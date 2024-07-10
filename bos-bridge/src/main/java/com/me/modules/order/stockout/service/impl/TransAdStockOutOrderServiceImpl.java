package com.me.modules.order.stockout.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.exception.BusinessException;
import com.me.common.utils.DateUtils;
import com.me.modules.finance.service.FinanceService;
import com.me.modules.order.mtransfer.entity.MTransfer;
import com.me.modules.order.mtransfer.entity.MTransferItem;
import com.me.modules.order.mretail.dto.QryMRetailItemRespDto;
import com.me.modules.order.dto.QryMRetailPayItemRespDto;
import com.me.modules.order.mtransfer.dto.QryStockOutMTransferItemDataRespDto;
import com.me.modules.order.mapper.DateMapper;
import com.me.modules.order.profit.dto.QryProfitReqDto;
import com.me.modules.order.profit.dto.QryProfitRespDto;
import com.me.modules.order.profit.service.OrderProfitService;
import com.me.modules.order.sku.service.SkuRelService;
import com.me.modules.order.stockout.service.TransAdStockOutOrderService;
import com.me.modules.sys.mapper.IdMapper;
import com.me.modules.order.mretail.entity.MRetail;
import com.me.modules.order.mretail.entity.MRetailItem;
import com.me.modules.order.mretail.entity.MRetailPayItem;
import com.me.modules.order.mretail.service.MRetailItemService;
import com.me.modules.order.mretail.service.MRetailPayItemService;
import com.me.modules.order.mretail.service.MRetailService;
import com.me.modules.order.mtransfer.service.MTransferItemService;
import com.me.modules.order.mtransfer.service.MTransferService;
import com.me.modules.order.service.*;
import com.me.modules.sys.service.HttpService;
import com.me.modules.order.shop.entity.ShopRel;
import com.me.modules.order.shop.service.ShopRelService;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOutList;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutListService;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutService;
import com.me.modules.order.store.entity.CStore;
import com.me.modules.order.store.entity.StoreRel;
import com.me.modules.order.store.service.CStoreService;
import com.me.modules.order.store.service.StoreRelService;
import com.me.modules.order.sku.entity.SkuRel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TransAdStockOutOrderServiceImpl implements TransAdStockOutOrderService {

    private HttpService httpService;

    private WangDianAbroadStockOutService wangDianAbroadStockOutService;

    private WangDianAbroadStockOutListService wangDianAbroadStockOutListService;

    private SkuRelService skuRelService;

    private MRetailService MRetailService;

    private MTransferItemService mTransferItemService;

    private MRetailPayItemService mRetailPayItemService;

    private MRetailItemService mRetailItemService;

    private IdMapper idMapper;

    private MTransferService mTransferService;

    private DateMapper dateMapper;

    private CStoreService cStoreService;

    private ShopRelService shopRelService;

    private StoreRelService storeRelService;

    private FinanceService financeService;

    private OrderProfitService orderProfitService;

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";


    @Override
    public void tranAbroadDetailOrder(List<String> tradeIds) {
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/trade/v1/getSalesTradeOrderList";
        Map<String,String> header = new HashMap<>();
        header.put("appName",appName);
        header.put("sid",sid);
        long time = (System.currentTimeMillis() / 1000);
        String timestamp = String.valueOf(time);
        header.put("timestamp",timestamp);

        Map<String,String> signMap = new TreeMap<>();
        signMap.put("sid", sid);
        signMap.put("appName", appName);
        signMap.put("timestamp",timestamp);

        Map<String,Object> body = new HashMap<>();
        body.put("tradeIds",tradeIds);

        signMap.put("body", JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        Map<String,String> parameter = new HashMap<>();
        parameter.put("sid", sid);
        parameter.put("appName", appName);
        parameter.put("timestamp",timestamp);
        parameter.put("sign",sign);

        String fullUrl = baseUrl+"?"+httpService.urlencode(parameter);
        log.info("fullUrl="+fullUrl);
        log.info(JSON.toJSONString(body));
        String response = httpService.sendPostRequest(fullUrl,header,body);
        log.info("response="+response);

        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONArray("data");
        List<WangDianAbroadStockOutList> wangDianAbroadStockOutLists = new ArrayList<>();
        for(int i=0;i<dataResultArray.size();i++){
            JSONObject jsonObject = dataResultArray.getJSONObject(i);
            String STOCKOUTID = jsonObject.getString("tradeId");

            //获取erp系统的商家编码
            String SPECNO = jsonObject.getString("skuNo");
            QueryWrapper<SkuRel> erpSkuQueryWrapper = new QueryWrapper<>();
            erpSkuQueryWrapper.eq("ABROAD_SKU",SPECNO);
            SkuRel skuRel = skuRelService.getOne(erpSkuQueryWrapper);
            if(skuRel != null){
                SPECNO = skuRel.getErpSku();
            }
            SPECNO = SPECNO.trim();
            //获取小计
            QueryWrapper<WangDianAbroadStockOut> abroadStockOutQueryWrapper = new QueryWrapper<>();
            abroadStockOutQueryWrapper.eq("STOCKOUTID",STOCKOUTID);
            WangDianAbroadStockOut abroadStockOut = wangDianAbroadStockOutService.getOne(abroadStockOutQueryWrapper);
            String orderNo = abroadStockOut.getORDERNO();

            log.info("yj="+orderNo);
            //从月结利润报表-订单明细接口 获取小计
            String SELLPRICE = financeService.getFinanceReport(orderNo,jsonObject.getString("apiSpecNo"));


            String GIFTTYPE = jsonObject.getString("isGift");
            String SHAREPOST = jsonObject.getString("orderPostAmount");
            String SRCOID = jsonObject.getString("srcOid");
            String SRCTID = jsonObject.getString("srcTid");
            String NUM = jsonObject.getString("num");
            String MARKETPRICE = jsonObject.getString("price");

            WangDianAbroadStockOutList wangDianAbroadStockOutList = new WangDianAbroadStockOutList();
            wangDianAbroadStockOutList.setSTOCKOUTID(STOCKOUTID);
            wangDianAbroadStockOutList.setSPECNO(SPECNO);
            wangDianAbroadStockOutList.setSELLPRICE(SELLPRICE);
            wangDianAbroadStockOutList.setGIFTTYPE(GIFTTYPE);
            wangDianAbroadStockOutList.setSHAREPOST(SHAREPOST);
            wangDianAbroadStockOutList.setSRCOID(SRCOID);
            wangDianAbroadStockOutList.setSRCTID(SRCTID);
            wangDianAbroadStockOutList.setNUM(NUM);
            wangDianAbroadStockOutList.setMARKETPRICE(MARKETPRICE);

            wangDianAbroadStockOutLists.add(wangDianAbroadStockOutList);
        }
        wangDianAbroadStockOutListService.saveBatch(wangDianAbroadStockOutLists);
    }

    @Override
    public Map<String, Object> getStockOutOrderDetails(Integer currentPageNo) {
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/stock/v1/getStockOutOrderDetails";

        long time = (System.currentTimeMillis() / 1000);
        String timestamp = String.valueOf(time);

        Map<String,String> header = new HashMap<>();
        header.put("appName",appName);
        header.put("sid",sid);
        header.put("timestamp",timestamp);

        Map<String,String> signMap = new TreeMap<>();
        signMap.put("sid", sid);
        signMap.put("appName", appName);
        signMap.put("timestamp",timestamp);

        //JSONObject body = new JSONObject();
        Map<String,Object> body = new HashMap<>();
        body.put("pageNo",currentPageNo);
        body.put("pageSize",200);
        body.put("order_status",95);
        body.put("status",0);
//        body.put("start_time","2023-12-19 00:00:00");
//        body.put("end_time","2023-12-19 23:59:59");
        //JY202312010003
        body.put("start_time","2024-01-01 00:00:00");
        body.put("end_time","2024-01-01 10:30:03");

        signMap.put("body", JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = httpService.sendPostRequest(baseUrl,header,body);
        //log.info("response="+response);

        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        Boolean empty = dataJsonObject.getJSONObject("data").getBoolean("empty");
        Integer pageSize = dataJsonObject.getJSONObject("data").getInteger("pageSize");
        Integer total = dataJsonObject.getJSONObject("data").getInteger("total");
        HashMap<String,Object> resMap = new HashMap<>();
        resMap.put("empty",empty);
        resMap.put("pageSize",pageSize);
        resMap.put("total",total);

        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONArray("data");
        List<WangDianAbroadStockOut> stockOuts = new ArrayList<>();
        List<WangDianAbroadStockOutList> stockOutDetails = new ArrayList<>();
        for(int i=0;i<dataResultArray.size();i++){
            JSONObject jsonObject = dataResultArray.getJSONObject(i);
            String stockoutId = jsonObject.getString("stockoutId");
            QueryWrapper<WangDianAbroadStockOut> stockOutQueryWrapper = new QueryWrapper<>();
            stockOutQueryWrapper.eq("STOCKOUTID",stockoutId);
            WangDianAbroadStockOut existData = wangDianAbroadStockOutService.getOne(stockOutQueryWrapper);
            if(existData == null){
                String stockoutNo = jsonObject.getString("stockoutNo");

                //处理仓库
                String warehouseNo = jsonObject.getString("warehouseNo");
                QueryWrapper<StoreRel> storeRelQueryWrapper = new QueryWrapper<>();
                storeRelQueryWrapper.eq("ABD_WAREHOUSE_NO",warehouseNo);
                StoreRel storeRel = storeRelService.getOne(storeRelQueryWrapper);
                warehouseNo = storeRel.getErpWareHouseNo();

                String consignTime = jsonObject.getString("consignTime");

                //处理店铺
                String shopName = jsonObject.getString("shopName");
                QueryWrapper<ShopRel> shopRelQueryWrapper = new QueryWrapper<>();
                shopRelQueryWrapper.eq("ABD_SHOPNAME",shopName);
                ShopRel shopRel = shopRelService.getOne(shopRelQueryWrapper);
                shopName = shopRel.getErpShopNAME();

                String srcOrderNo = jsonObject.getString("srcOrderNo");
                String orderStatus = jsonObject.getString("orderStatus");
                //log.info("stockoutId="+stockoutId+",stockoutNo="+stockoutNo+",srcOrderNo="+srcOrderNo+",warehouseNo="+warehouseNo+",consignTime="+consignTime+",shopName="+shopName);
                WangDianAbroadStockOut wangDianAbroadStockOut = new WangDianAbroadStockOut();
                wangDianAbroadStockOut.setSTOCKOUTID(stockoutId);
                wangDianAbroadStockOut.setORDERNO(srcOrderNo);
                wangDianAbroadStockOut.setWAREHOUSENO(warehouseNo);
                wangDianAbroadStockOut.setCONSIGNTIME(consignTime);

                //11:海外网店销售
                wangDianAbroadStockOut.setTRADETYPE("11");

                wangDianAbroadStockOut.setSHOPNAME(shopName);
                wangDianAbroadStockOut.setSRCTIDS(stockoutNo);
                wangDianAbroadStockOut.setTRADESTATUS(orderStatus);
                stockOuts.add(wangDianAbroadStockOut);

                //解析明细接口
                JSONArray detailArray = jsonObject.getJSONArray("stockOutOrderDetailsVOList");
                for(int j=0;j<detailArray.size();j++){
                    WangDianAbroadStockOutList stockOutDetail = new WangDianAbroadStockOutList();
                    JSONObject detailObj = detailArray.getJSONObject(j);
                    String specNo = detailObj.getString("specNo");
                    String num = detailObj.getString("num");
                    stockOutDetail.setSTOCKOUTID(stockoutId);
                    stockOutDetail.setSPECNO(specNo);
                    // 待利润报表开发后获取收入小计作为销售价
                    QryProfitReqDto reqDto = new QryProfitReqDto();
                    reqDto.setSkuSearchVal(specNo);
                    reqDto.setNumberVal(srcOrderNo);

                    QryProfitRespDto respDto = orderProfitService.queryProfit(reqDto);
                    stockOutDetail.setSELLPRICE(respDto.getTotalIncome());

                    //默认邮费为0
                    stockOutDetail.setSHAREPOST("0");
                    stockOutDetail.setSRCOID(respDto.getSrcOid());
                    stockOutDetail.setSRCTID(respDto.getTid());
                    stockOutDetail.setNUM(num);
                    stockOutDetails.add(stockOutDetail);
                }

            }
        }
        if(stockOuts.size()>0){
            wangDianAbroadStockOutService.saveBatch(stockOuts);
        }
        if(stockOutDetails.size()>0){
            wangDianAbroadStockOutListService.saveBatch(stockOutDetails);
        }
        return resMap;
    }

    @Override
    public void tranMRetail() {
        Integer v_count;
        Integer v_origstore_id;
        Integer v_customer_id;
        Integer v_deststore_id;
        String v_is_special;
        BigDecimal v_special_discount;
        List<WangDianAbroadStockOut> orderList = wangDianAbroadStockOutService.queryUnTransWangDianAbroadStockOut();
        log.info("orderList="+orderList.toString());
        for (WangDianAbroadStockOut order: orderList){
            try {
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
            }catch (Exception e){
                log.info(e.getMessage());
                
            }
        }
    }


}
