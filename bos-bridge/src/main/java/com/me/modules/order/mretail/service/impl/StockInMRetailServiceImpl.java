package com.me.modules.order.mretail.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.exception.BusinessException;
import com.me.common.utils.DateUtils;
import com.me.modules.order.customer.dto.QueryIdAndCodeRespDto;
import com.me.modules.order.customer.service.CustomerService;
import com.me.modules.order.mapper.DateMapper;
import com.me.modules.order.mretail.dto.TransStockInMRetailReqDto;
import com.me.modules.order.mretail.service.MRetailService;
import com.me.modules.order.mretail.service.StockInMRetailService;
import com.me.modules.order.msale.service.MSaleService;
import com.me.modules.order.mtransfer.dto.QryStockInMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.dto.QryStockOutMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.entity.MTransfer;
import com.me.modules.order.mtransfer.entity.MTransferItem;
import com.me.modules.order.mtransfer.service.MTransferItemService;
import com.me.modules.order.mtransfer.service.MTransferService;
import com.me.modules.order.stockin.entity.WangDianAbroadStockIn;
import com.me.modules.order.stockin.service.WangDianAbroadStockInListService;
import com.me.modules.order.stockin.service.WangDianAbroadStockInService;
import com.me.modules.order.store.entity.CStore;
import com.me.modules.order.store.service.CStoreService;
import com.me.modules.sys.mapper.IdMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StockInMRetailServiceImpl implements StockInMRetailService {

    
    private WangDianAbroadStockInService wangDianAbroadStockInService;

    
    private WangDianAbroadStockInListService wangDianAbroadStockInListService;

    
    private CStoreService cStoreService;

    
    private MTransferService mTransferService;

    private MTransferItemService mTransferItemService;

    
    private MSaleService mSaleService;

    
    private CustomerService customerService;

    
    private MRetailService mRetailService;

    
    private IdMapper idMapper;

    
    private DateMapper dateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transStockInMRetail(WangDianAbroadStockIn order, TransStockInMRetailReqDto reqDto) {
        Integer v_origstore_id = reqDto.getV_origstore_id();
        Integer v_deststore_id = reqDto.getV_deststore_id();
        Integer v_customer_id = reqDto.getV_customer_id();
        String v_sku = reqDto.getV_sku();
        Integer v_m_transfer_id = reqDto.getV_m_transfer_id();

        Integer v_count = reqDto.getV_count();
        v_count = mSaleService.queryCount(order.getTid());
        QueryIdAndCodeRespDto respDto = customerService.queryIdAndCode(order.getRemark());
        v_customer_id = respDto.getMaxId();
        //执行中间表转零售单退单逻辑
        if( v_count==0 && v_customer_id==0) {
            String wareHouseNo = order.getWarehouseNo();
            //增加控制：如果drpstore在PORTAL中没有对应的店仓编码，不允许。
            CStore cStore_1 = cStoreService.queryIds(wareHouseNo);
            if (cStore_1 == null) {
                throw new BusinessException("20201", order.getWarehouseName() + "在ERP中没有对应的店仓编码，不允许!");
            }
            v_origstore_id = cStore_1.getId();
            v_customer_id = cStore_1.getCCustomerId();

            //增加控制：如果DrpStoreName在PORTAL中没有对应的店仓编码，不允许。

            CStore cStore_2 = cStoreService.queryCstoreByShopName(order.getShopName());
            //等接口修改完之前打开异常代码注释
//            if(cStore_2 == null){
//                throw new  BusinessException("20201",order.getShopName()+"DRP零售单店仓编码在ERP中没有对应的店仓编号或者"+order.getTid()+"该原单不存在，不允许!");
//            }
            v_deststore_id = cStore_2.getId();

            //增加控制：如果当前记录没有对应的明细，不允许。
            v_count = wangDianAbroadStockInListService.queryCount(order.getStockinId());
            if (v_count == 0) {
                throw new BusinessException("20201", "当前记录没有对应的明细，不允许!");
            }
            //增加控制：如果当前记录对应的明细中的条码在PORTAL中不存在，不允许。
            v_sku = wangDianAbroadStockInListService.querySku(order.getStockinId());
            if (StrUtil.isNotEmpty(v_sku)) {
                throw new BusinessException("20201", "当前记录对应的明细中的条码" + v_sku + "在ERP中不存在，不允许!");
            }

            v_count = mRetailService.queryCount2(order.getOrderNo());
            if (v_count > 0) {
                throw new BusinessException("20201", order.getOrderNo() + "以重复，不允许!");
            }

            //获取调拨单ID
            v_m_transfer_id = idMapper.get_sequences("m_transfer");
            log.info("v_m_transfer_id=" + v_m_transfer_id);

            MTransfer mTransfer = new MTransfer();
            //封装数据
            mTransfer.setId(v_m_transfer_id);
            mTransfer.setAdClientId(37);
            mTransfer.setAdOrgId(27);
            mTransfer.setOwnerId(893);
            mTransfer.setModifierId(893);
            Date currentDateTime = dateMapper.getCurrentDate();
            mTransfer.setCreationDate(currentDateTime);
            mTransfer.setModifiedDate(currentDateTime);
            mTransfer.setIsActive('Y');
            String docNo = idMapper.get_sequenceno("TF", 37);
            mTransfer.setDocNo(docNo);
            mTransfer.setDocType("SUO");
            mTransfer.setCOrigId(v_deststore_id);
            mTransfer.setCDestId(v_origstore_id);
            mTransfer.setDescription("由旺店通退货订单:" + order.getTid() + "入库生成。");
            mTransfer.setTransferType("NOR");
            //处理出货时间 输出yyyyMMdd格式
            Integer date = DateUtils.formatConversion(order.getCheckTime());
            mTransfer.setBillDate(date);
            mTransfer.setDateIN(date);
            mTransfer.setDateOUT(date);
            mTransfer.setTransferNo(order.getOrderNo());
            mTransfer.setOrderNo(order.getOrderNo());

            mTransferService.save(mTransfer);

            mTransferService.mTransferAc(v_m_transfer_id);


            List<QryStockInMTransferItemDataRespDto> respDtoList = mTransferItemService.queryStockInMTransferItemData(order.getStockinId());

            for( QryStockInMTransferItemDataRespDto QryRespDto: respDtoList){
                MTransferItem mTransferItem = new MTransferItem();
                //封装数据
                mTransferItem.setId(idMapper.get_sequences("m_transferitem"));
                mTransferItem.setAdClientId(37);
                mTransferItem.setAdOrgId(27);
                mTransferItem.setOwnerId(893);
                mTransferItem.setModifierId(893);
                mTransferItem.setCreationdate(currentDateTime);
                mTransferItem.setModifiedDate(currentDateTime);
                mTransferItem.setIsActive("Y");
                mTransferItem.setMTransferId(v_m_transfer_id);
                mTransferItem.setMProductId(QryRespDto.getMProductId());
                mTransferItem.setMAttributeSetInstanceId(QryRespDto.getMAttributeSetInstanceId());
                mTransferItem.setQtyOut(QryRespDto.getQtyOut());
                mTransferItem.setQtyIn(QryRespDto.getQtyIn());
                mTransferItem.setQty(QryRespDto.getQty());
                mTransferItemService.save(mTransferItem);
            }

            QueryWrapper<MTransferItem> mTransferItemQry = new QueryWrapper<>();
            mTransferItemQry.eq("m_transfer_id",v_m_transfer_id);
            List<MTransferItem> mTransferItems= mTransferItemService.list(mTransferItemQry);
            for(MTransferItem element: mTransferItems){
                mTransferItemService.mTransferItemAcm(element.getId());
            }



//            int i = 5;
//            i = 5/0;
        }
    }
}
