package com.me.bos;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.exception.BusinessException;
import com.me.common.utils.DateUtils;
import com.me.modules.order.mtransfer.entity.MTransfer;
import com.me.modules.order.mtransfer.entity.MTransferItem;
import com.me.modules.order.mretail.dto.QryMRetailItemRespDto;
import com.me.modules.order.dto.QryMRetailPayItemRespDto;
import com.me.modules.order.mtransfer.dto.QryStockOutMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.service.MTransferItemService;
import com.me.modules.order.mtransfer.service.MTransferService;
import com.me.modules.order.shop.entity.ShopRel;
import com.me.modules.order.shop.service.ShopRelService;
import com.me.modules.order.store.mapper.CStoreMapper;
import com.me.modules.order.mapper.DateMapper;
import com.me.modules.sys.mapper.IdMapper;
import com.me.modules.order.mretail.entity.MRetail;
import com.me.modules.order.mretail.entity.MRetailItem;
import com.me.modules.order.mretail.entity.MRetailPayItem;
import com.me.modules.order.mretail.service.MRetailItemService;
import com.me.modules.order.mretail.service.MRetailPayItemService;
import com.me.modules.order.mretail.service.MRetailService;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutListService;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutService;
import com.me.modules.order.store.entity.CStore;
import com.me.modules.order.store.entity.StoreRel;
import com.me.modules.order.store.service.CStoreService;
import com.me.modules.order.store.service.StoreRelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class BosTest1_1 {

    @Autowired
    private WangDianAbroadStockOutService wangDianAbroadStockOutService;

    @Autowired
    private WangDianAbroadStockOutListService wangDianAbroadStockOutListService;

    @Autowired
    private MRetailService MRetailService;

    @Autowired
    private CStoreService cStoreService;

    @Autowired
    private StoreRelService storeRelService;

    @Autowired
    private CStoreMapper cStoreMapper;

    @Autowired
    private IdMapper idMapper;

    @Autowired
    private MTransferService mTransferService;

    @Autowired
    private DateMapper dateMapper;

    @Autowired
    private ShopRelService shopRelService;

    @Autowired
    private MTransferItemService mTransferItemService;

    @Autowired
    private MRetailItemService mRetailItemService;

    @Autowired
    private MRetailPayItemService mRetailPayItemService;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void transAbroadStockOutJob(){
        Integer v_count;
        Integer v_origstore_id;
        Integer v_customer_id;
        Integer v_deststore_id;
        String v_is_special;
        BigDecimal v_special_discount;
        List<WangDianAbroadStockOut> outOrderList = wangDianAbroadStockOutService.queryUnTransWangDianAbroadStockOut();
        log.info("outOrderList="+outOrderList.toString());
        for (WangDianAbroadStockOut order : outOrderList){
            String orderNo = order.getORDERNO();
            String stockOutId = order.getSTOCKOUTID();
            String wareHouseNo = order.getWAREHOUSENO();
            v_count = MRetailService.queryCount1(orderNo);
            if(v_count>0){
                throw new BusinessException("20201",orderNo+"该订单已生成，不允许！");
            }
            //查询仓库映射表,获取erp的店仓编码
            QueryWrapper<StoreRel> storeRelQueryWrapper = new QueryWrapper<>();
            storeRelQueryWrapper.eq("ABD_WAREHOUSE_NO",wareHouseNo);
            StoreRel storeRel = storeRelService.getOne(storeRelQueryWrapper);
            if(storeRel == null){
                throw new  BusinessException("20201",order.getWAREHOUSENAME()+"在ERP中没有对应的店仓编码，不允许!");
            }

            //增加控制：如果drpstore在PORTAL中没有对应的店仓编码，不允许。
            //查看epr店仓编码是否存在于db中
            CStore cStore_1 = cStoreService.queryIds(storeRel.getErpWareHouseNo());
            if(cStore_1 == null){
                throw new  BusinessException("20201",order.getWAREHOUSENAME()+"在ERP中没有对应的店仓编码，不允许!");
            }
            //log.info(cStore_1.toString());
            v_origstore_id = cStore_1.getId();
            v_customer_id = cStore_1.getCCustomerId();


            //增加控制：如果DrpStoreName在PORTAL中没有对应的店仓编码，不允许。
            //获取erp店仓名称
            QueryWrapper<ShopRel> shopRelQueryWrapper = new QueryWrapper<>();
            shopRelQueryWrapper.eq("ABD_SHOPNAME",order.getSHOPNAME());
            ShopRel shopRel = shopRelService.getOne(shopRelQueryWrapper);
            log.info(shopRel.getErpShopNAME());




            //
            String erpShopName = shopRel.getErpShopNAME();
            CStore cStore_2 = cStoreService.queryCstoreByShopName(erpShopName);
            v_deststore_id = cStore_2.getId();
            v_is_special = cStore_2.getIsSpecial();
            v_special_discount = cStore_2.getSpecialDiscount();
            if(cStore_2 == null){
                throw new  BusinessException("20201",order.getSHOPNAME()+"DRP零售单店仓编码在ERP中没有对应的店仓编号!");
            }

            //增加控制：如果当前记录没有对应的明细，不允许。
            String count = wangDianAbroadStockOutListService.queryCount(stockOutId);
            if("0".equals(count)){
                throw new BusinessException("20201","当前记录没有对应的明细，不允许!");
            }

            //增加控制：如果当前记录对应的明细中的条码在PORTAL中不存在，不允许。
            String sku = wangDianAbroadStockOutListService.querySku(stockOutId);
            if(StrUtil.isNotBlank(sku)){
                throw new BusinessException("20201",stockOutId+"当前记录对应的明细中的条码"+sku+"在ERP中不存在，不允许!");
            }

            //获取调拨单ID
            Integer v_m_transfer_id = idMapper.get_sequences("m_transfer");

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
            String docNo = idMapper.get_sequenceno("TF",37);
            mTransfer.setDocNo(docNo);
            mTransfer.setDocType("SUO");
            mTransfer.setCOrigId(v_origstore_id);
            mTransfer.setCDestId(v_deststore_id);
            mTransfer.setDescription("由海外旺店通订单:"+order.getSRCTIDS()+"出库生成。");
            mTransfer.setTransferType("NOR");
            //处理出货时间 输出yyyyMMdd格式
            Integer date = DateUtils.formatConversion(order.getCONSIGNTIME());
            mTransfer.setBillDate(date);
            mTransfer.setDateIN(date);
            mTransfer.setDateOUT(date);
            mTransfer.setTransferNo(orderNo);
            mTransfer.setOrderNo(orderNo);

            mTransferService.save(mTransfer);

            //执行存储过程 提交调拨单
            mTransferService.mTransferAc(v_m_transfer_id);

            //wangdian_stockout_list、m_product_alias、m_product联合查询

            //QueryMTransferItemDataRespDto respDto = mTransferItemService.queryMTransferItemData(order.getSTOCKOUTID());

//            MTransferItem mTransferItem = new MTransferItem();
//            //封装数据
//            mTransferItem.setId(idMapper.get_sequences("m_transferitem"));
//            mTransferItem.setAdClientId(37);
//            mTransferItem.setAdOrgId(27);
//            mTransferItem.setOwnerId(893);
//            mTransferItem.setModifierId(893);
//            mTransferItem.setCreationdate(currentDateTime);
//            mTransferItem.setModifiedDate(currentDateTime);
//            mTransferItem.setIsActive("Y");
//            mTransferItem.setMTransferId(v_m_transfer_id);
//            mTransferItem.setMProductId(respDto.getMProductId());
//            mTransferItem.setMAttributeSetInstanceId(respDto.getMAttributeSetInstanceId());
//            mTransferItem.setQtyOut(respDto.getQtyOut());
//            mTransferItem.setQtyIn(respDto.getQtyIn());
//            mTransferItem.setQty(respDto.getQty());
//            mTransferItem.setMDim4Id(respDto.getMDim4Id());
//            mTransferItem.setPreCost(respDto.getPreCost());
//            mTransferItem.setPriceList(respDto.getPriceList());
//
//            mTransferItemService.save(mTransferItem);

            List<QryStockOutMTransferItemDataRespDto> respDtoList = mTransferItemService.queryStockOutMTransferItemData(order.getSTOCKOUTID());
            //List<MTransferItem> mTransferItemList = new ArrayList<>();
            for( QryStockOutMTransferItemDataRespDto respDto: respDtoList){
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
                mTransferItem.setMProductId(respDto.getMProductId());
                mTransferItem.setMAttributeSetInstanceId(respDto.getMAttributeSetInstanceId());
                mTransferItem.setQtyOut(respDto.getQtyOut());
                mTransferItem.setQtyIn(respDto.getQtyIn());
                mTransferItem.setQty(respDto.getQty());
                mTransferItem.setMDim4Id(respDto.getMDim4Id());
                mTransferItem.setPreCost(respDto.getPreCost());
                mTransferItem.setPriceList(respDto.getPriceList());
                mTransferItemService.save(mTransferItem);
            }
            mTransferItemService.updateMTransferItem(v_m_transfer_id);
            //调用存储过程：M_TRANSFER_AM
            mTransferService.mTransferItemAm(v_m_transfer_id);
            //调用存储过程：M_TRANSFER_SUBMIT
            mTransferService.mTransSubmit(v_m_transfer_id);
            //自动匹配出库数量：M_OUT_QTYCOP
            Integer p_user_id = 893;
            mTransferService.mTransferOutQtyCop(v_m_transfer_id,p_user_id);
            // 出库提交：M_TRANSFEROUT_SUBMIT
            mTransferService.mTransferOutSubmit(v_m_transfer_id);
            // 自动匹配入库数量：M_IN_QTYCOP
            mTransferService.mTransferInQtyCop(v_m_transfer_id,p_user_id);
            // 入库提交：M_TRANSFERIN_SUBMIT
            mTransferService.mTransferInSubmit(v_m_transfer_id);

            // 和生成一张已提交的零售单
            Integer v_retail_id = idMapper.get_sequences("M_RETAIL");

            //封装零售单数据
            MRetail mRetail = new MRetail();
            mRetail.setId(v_retail_id);
            mRetail.setAdClientId(37);
            mRetail.setAdOrgId(27);
            mRetail.setCreationDate(currentDateTime);
            mRetail.setModifiedDate(currentDateTime);
            mRetail.setOwnerId(893);
            mRetail.setModifierId(893);
            mRetail.setIsActive("Y");
            mRetail.setDocNo(idMapper.get_sequenceno("RE",37));
            mRetail.setBillDate(date);
            mRetail.setDateOUT(date);
            mRetail.setDateIN(date);
            mRetail.setCStoreId(v_deststore_id);
            mRetail.setStatus(1);
            mRetail.setDescription("由海外旺店通订单:"+order.getSRCTIDS()+"生成。");
            mRetail.setRetailBillType("CMR");
            mRetail.setRefNo("C"+order.getSRCTIDS());
            mRetail.setOmsSourceCode(order.getSRCTIDS());
            mRetail.setIsInTl("N");
            mRetail.setOrderNo(order.getORDERNO());
            mRetail.setTradeType(order.getTRADETYPE());
            mRetail.setWdtCStore(order.getSHOPNAME());
            MRetailService.save(mRetail);

            //执行m_retail_ac存储过程
            MRetailService.mRetailAc(v_retail_id);

            log.info(order.getSTOCKOUTID());
            List<QryMRetailItemRespDto> respDtoList1 = mRetailItemService.queryMRetailItem(order.getSTOCKOUTID());
            List<MRetailItem> mRetailItemList = new ArrayList<>();
            for(QryMRetailItemRespDto retailItemRespDto :respDtoList1){
                MRetailItem mRetailItem = new MRetailItem();
                mRetailItem.setId(idMapper.get_sequences("m_retailitem"));
                mRetailItem.setAdClientId(37);
                mRetailItem.setAdOrgId(27);
                mRetailItem.setCreationDate(currentDateTime);
                mRetailItem.setModifiedDate(currentDateTime);
                mRetailItem.setOwnerId(893);
                mRetailItem.setModifierId(893);
                mRetailItem.setIsActive("Y");
                mRetailItem.setMRetailId(v_retail_id);
                mRetailItem.setMProductAliasId(retailItemRespDto.getId());
                mRetailItem.setMProductId(retailItemRespDto.getMProductId());
                mRetailItem.setMAttributeSetInstanceId(retailItemRespDto.getMAttributeSetInstanceId());
                String num = retailItemRespDto.getNum();
                mRetailItem.setQty(num);
                BigDecimal priceList = retailItemRespDto.getPriceList();
                mRetailItem.setPriceList(priceList);
                BigDecimal priceActual;
                if("Y".equals(v_is_special)){
                    priceActual = priceList.multiply(v_special_discount);
                }else {
                    BigDecimal sellPrice = new BigDecimal(retailItemRespDto.getSellPrice());
                    priceActual = sellPrice.setScale(2, RoundingMode.HALF_UP);
                }
                mRetailItem.setPriceActual(priceActual);
                BigDecimal num1 = new BigDecimal(num);
                mRetailItem.setTotAmtActual(num1.multiply(priceList));
                mRetailItem.setStatus(1);
                mRetailItem.setTotAmtActual(num1.multiply(priceActual).setScale(2, RoundingMode.HALF_UP));
                mRetailItem.setType(1);
                mRetailItem.setCVipId("");
                mRetailItem.setSrcTid(retailItemRespDto.getSrcTid());
                mRetailItem.setSrcOid(retailItemRespDto.getSrcOid());
                mRetailItem.setSharePost(retailItemRespDto.getSharePost());
                //mRetailItemList.add(mRetailItem);
                mRetailItemService.save(mRetailItem);
            }
            //mRetailItemService.saveBatch(mRetailItemList);
            mRetailItemService.updateMRetailItem(v_retail_id);
            MRetailService.updateMRetail(v_retail_id);

            QryMRetailPayItemRespDto retailPayItemRespDto = mRetailPayItemService.queryMRetailPayItem(v_retail_id);

            MRetailPayItem mRetailPayItem = new MRetailPayItem();
            mRetailPayItem.setId(idMapper.get_sequences("M_RETAILPAYITEM"));
            mRetailPayItem.setAdClientId(37);
            mRetailPayItem.setAdOrgId(27);
            mRetailPayItem.setOwnerId(893);
            mRetailPayItem.setModifierId(893);
            mRetailPayItem.setCreationdate(currentDateTime);
            mRetailPayItem.setModifiedDate(currentDateTime);
            mRetailPayItem.setIsActive("Y");
            mRetailPayItem.setMRetailId(v_retail_id);
            mRetailPayItem.setCPayWayId(retailPayItemRespDto.getCPayWayId());
            mRetailPayItem.setPayAmount(retailPayItemRespDto.getTotAmtActual());

            mRetailPayItemService.save(mRetailPayItem);

            MRetailService.mRetailSubmit(v_retail_id);

        }
    }
}
