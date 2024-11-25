package com.me.modules.json.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.me.common.config.K3Config;
import com.me.modules.assembly.dto.PutAssemblyDto;
import com.me.modules.assembly.jsonpojo.AssyFEntityJson;
import com.me.modules.assembly.jsonpojo.AssyFsubFntityJson;
import com.me.modules.assembly.jsonpojo.AssyJson;
import com.me.modules.assembly.jsonpojo.AssyModelJson;
import com.me.modules.assembly.pojo.AssyFEntity;
import com.me.modules.assembly.pojo.AssyFSubEntity;
import com.me.modules.json.service.JsonService;
import com.me.modules.purchase.dto.PutPurReqDto;
import com.me.modules.purchase.jsonpojo.*;
import com.me.modules.purchase.pojo.PurFEntry;
import com.me.modules.retpur.dto.GetPutRePurReqDto;
import com.me.modules.retpur.jsonpojo.FPURMRB;
import com.me.modules.retpur.jsonpojo.FPURMRBFIN;
import com.me.modules.retpur.pojo.RePurFentity;
import com.me.modules.resale.dto.PutReSaleReqDto;
import com.me.modules.resale.jsonpojo.SalInFEntityJson;
import com.me.modules.resale.jsonpojo.SalInJson;
import com.me.modules.resale.jsonpojo.SalInModelJson;
import com.me.modules.resale.jsonpojo.SaleInSubHeadEntityJson;
import com.me.modules.resale.pojo.ReSaleFEntity;
import com.me.modules.resale.service.K3RetSaleItemService;
import com.me.modules.sale.dto.PutSaleReqDto;
import com.me.modules.sale.jsonpojo.*;
import com.me.modules.sale.pojo.SaleFEntity;

import com.me.modules.retpur.jsonpojo.RetPurJson;
import com.me.modules.retpur.jsonpojo.RePurModelJson;


import com.me.modules.tranfer.dto.PutTrfReqDto;
import com.me.modules.tranfer.jsonpojo.FBillEntryJson;
import com.me.modules.tranfer.jsonpojo.TrfJson;
import com.me.modules.tranfer.jsonpojo.TrfModelJson;
import com.me.modules.tranfer.pojo.TrfFEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class JsonServiceImpl implements JsonService {


    private K3RetSaleItemService k3RetSaleItemService;


    private K3Config k3Config;

    @Override
    public String getSaveSaleJsons(PutSaleReqDto dto) {

        List<Map<String,String>> params = new ArrayList<>();

        //String id = k3MSale.getID();

        String fbilltypeid = "XSCKD01_SYS";
        String fbillno = dto.getFbillno();
        String fdate = dto.getFdate();
        String fstockorgid = dto.getFstockorgid();
        String fsaleorgid = dto.getFsaleorgid();
        String fsettleorgid = dto.getFsettleorgid();
        String fthirdbillno = dto.getFthirdbillno();
        String fsettlecurrid = dto.getFsettlecurrid();
        String fownertypeidhead = "业务组织";

        String fcustomerid = dto.getFcustomerid();

        String fDsgFllx = "留空";

        String fdsgbase = dto.getFdsgbase();
        String fDsgText1 = "";


        SalOutJson salOutJson = new SalOutJson();
        salOutJson.setIsDeleteEntry("true");
        salOutJson.setSubSystemId("0");
        salOutJson.setIsVerifyBaseDataField("false");
        salOutJson.setIsEntryBatchFill("true");
        salOutJson.setValidateFlag("true");
        salOutJson.setNumberSearch("true");
        salOutJson.setIsAutoAdjustField("false");
        //测试用false
        salOutJson.setIsAutoSubmitAndAudit("true");

        salOutJson.setInterationFlags("STK_InvCheckDetailMessage");
        salOutJson.setIgnoreInterationFlag("");
        salOutJson.setIsControlPrecision("false");
        salOutJson.setValidateRepeatJson("false");

        SalOutModelJson salOutModelJson = new SalOutModelJson();
        salOutModelJson.setFID("0");

        NumberJson fBillTypeIdJson = new NumberJson();
        if (StrUtil.isNotEmpty(fsaleorgid)) {
            fBillTypeIdJson.setFNumber(fbilltypeid);
        } else {
            fBillTypeIdJson.setFNumber("");
        }
        salOutModelJson.setFBillTypeID(fBillTypeIdJson);
        //fbillno = fbillno + "-001";
        salOutModelJson.setFBillNo(fbillno);

//            salOutModelJson.setFDate(fdate);
        //测试用的 固定时间戳
        salOutModelJson.setFDate(fdate);

        NumberJson fStockOrgIdJson = new NumberJson();
        if (StrUtil.isNotEmpty(fstockorgid)) {
            fStockOrgIdJson.setFNumber(fstockorgid);
        } else {
            fStockOrgIdJson.setFNumber("");
        }
        salOutModelJson.setFStockOrgId(fStockOrgIdJson);

        NumberJson fSaleOrgIdJson = new NumberJson();
        if (StrUtil.isNotEmpty(fsaleorgid)) {
            fSaleOrgIdJson.setFNumber(fsaleorgid);
        } else {
            fSaleOrgIdJson.setFNumber("");
        }

        salOutModelJson.setFSaleOrgId(fSaleOrgIdJson);

        salOutModelJson.setFOwnerTypeIdHead(fownertypeidhead);


        SaleOutSubHeadEntityJson saleOutSubHeadEntityJson = new SaleOutSubHeadEntityJson();

        //结算组织
        NumberJson fSettleOrgIDJson = new NumberJson();

        if (StrUtil.isNotEmpty(fsettleorgid)){
            fSettleOrgIDJson.setFNumber(fsettleorgid);
        }else {
            fSettleOrgIDJson.setFNumber("");
        }
        saleOutSubHeadEntityJson.setFSettleOrgID(fSettleOrgIDJson);
        saleOutSubHeadEntityJson.setFThirdBillNo(fthirdbillno);
        //saleOutSubHeadEntityJson.setFIsIncludedTax("false");

        //币别
        NumberJson fSettleCurrIDJson = new NumberJson();
        fSettleCurrIDJson.setFNumber(dto.getFsettlecurrid());
        saleOutSubHeadEntityJson.setFSettleCurrID(fSettleCurrIDJson);





        salOutModelJson.setSubHeadEntity(saleOutSubHeadEntityJson);

        NumberJson fCUSTOMERIDJson = new NumberJson();
        fCUSTOMERIDJson.setFNumber(fcustomerid);
        salOutModelJson.setFCUSTOMERID(fCUSTOMERIDJson);


        NumberJson setF_DSG_BaseJson = new NumberJson();
        if (StrUtil.isNotEmpty(fdsgbase)) {
            setF_DSG_BaseJson.setFNumber(fdsgbase);
        } else {
            setF_DSG_BaseJson.setFNumber("");
        }

        salOutModelJson.setF_DSG_Base(setF_DSG_BaseJson);

        salOutModelJson.setF_DSG_FLLX(fDsgFllx);

        if (StrUtil.isNotEmpty(fDsgText1)) {
            salOutModelJson.setF_DSG_Text1(fDsgText1);
        } else {
            salOutModelJson.setF_DSG_Text1("");
        }


        String freceiveaddress  = dto.getFreceiveaddress();
        if (StrUtil.isNotEmpty(freceiveaddress)){
            salOutModelJson.setFReceiveAddress(freceiveaddress);
        }


        List<SaleFEntity> FEntityList = dto.getFentitylist();

        List<SalOutFEntityJson> FEntity = new ArrayList<>();
        for (SaleFEntity saleFEntity : FEntityList) {

            SalOutFEntityJson salOutFEntityJson = new SalOutFEntityJson();

            salOutFEntityJson.setFEntryID("0");

            NumberJson fMaterialIdJson = new NumberJson();
            fMaterialIdJson.setFNumber(saleFEntity.getFmaterialId());
            salOutFEntityJson.setFMaterialId(fMaterialIdJson);

            salOutFEntityJson.setFEntryTaxRate(saleFEntity.getFentrytaxrate());
            salOutFEntityJson.setFRealQty(saleFEntity.getFrealqty());

            NumberJson fStockIdJson = new NumberJson();
            fStockIdJson.setFNumber(saleFEntity.getFstockid());
            salOutFEntityJson.setFStockId(fStockIdJson);

            salOutFEntityJson.setFTAXPRICE(saleFEntity.getFtaxprice());

            salOutFEntityJson.setFSoorDerno(saleFEntity.getFsoorderno());


            salOutFEntityJson.setF_DSG_srcoid(saleFEntity.getFdsgsrcoid());

            //默认批次效期、生产日期、效期
            NumberJson flotJson = new NumberJson();
            flotJson.setFNumber(k3Config.getFlot());
            salOutFEntityJson.setFLot(flotJson);
            salOutFEntityJson.setFExpiryDate(k3Config.getFExpiryDate());
            salOutFEntityJson.setFProduceDate(k3Config.getFProduceDate());

            FEntity.add(salOutFEntityJson);
        }

        salOutModelJson.setFEntity(FEntity);


        salOutJson.setModel(salOutModelJson);

        Gson gson = new Gson();
        String json = gson.toJson(salOutJson);
        //log.info("json=" + json);

        /*Map<String,String> paramsMap = new HashMap<>();
        //paramsMap.put("id",id);
        paramsMap.put("param",json);

        params.add(paramsMap);*/
        return json;
    }

    @Override
    public String getSaveReSalJsons(PutReSaleReqDto dto) {

        String fbillTypeId = "XSTHD01_SYS";
        String fbillNo = dto.getFbillno();
        String fdate = dto.getFdate();
        String fsaleOrgId = dto.getFsaleorgid();
        String fretCustId = dto.getFretcustid();
        String fstockOrgId = dto.getFstockorgid();


        String fsettleOrgId = dto.getFsettleorgid();
        String fsettleCurrId = dto.getFsettlecurrid();
        String fthirdBillNo = dto.getFthirdbillno();

        SalInJson salInJson = new SalInJson();
        salInJson.setCreator("demo");
        salInJson.setIsDeleteEntry("True");
        salInJson.setSubSystemId("0");
        salInJson.setIsVerifyBaseDataField("true");
        salInJson.setIsAutoAdjustField("true");
        //测试用false
        salInJson.setIsAutoSubmitAndAudit("true");
        salInJson.setInterationFlags("STK_InvCheckDetailMessage");

        SalInModelJson salInModelJson = new SalInModelJson();
        salInModelJson.setFID("0");

        NumberJson fBillTypeIDObj = new NumberJson();
        if (StrUtil.isNotEmpty(fbillTypeId)) {
            fBillTypeIDObj.setFNumber(fbillTypeId);
        } else {
            fBillTypeIDObj.setFNumber("");
        }
        salInModelJson.setFBillTypeID(fBillTypeIDObj);

        //fbillNo = fbillNo + "-001";
        salInModelJson.setFBillNo(fbillNo);
        salInModelJson.setFDate(fdate);

        NumberJson fSaleOrgIdObj = new NumberJson();
        if (StrUtil.isNotEmpty(fsaleOrgId)) {
            fSaleOrgIdObj.setFNumber(fsaleOrgId);
        } else {
            fSaleOrgIdObj.setFNumber("");
        }
        salInModelJson.setFSaleOrgId(fSaleOrgIdObj);

        NumberJson fStockOrgIdObj = new NumberJson();
        if (StrUtil.isNotEmpty(fstockOrgId)) {
            fStockOrgIdObj.setFNumber(fstockOrgId);
        } else {
            fStockOrgIdObj.setFNumber("");
        }
        salInModelJson.setFStockOrgId(fStockOrgIdObj);

        NumberJson fRetcustIdObj = new NumberJson();
        if (StrUtil.isNotEmpty(fretCustId)) {
            fRetcustIdObj.setFNumber(fretCustId);
        } else {
            fRetcustIdObj.setFNumber("");
        }
        salInModelJson.setFRetcustId(fRetcustIdObj);

        SaleInSubHeadEntityJson saleInSubHeadEntityJson = new SaleInSubHeadEntityJson();
        NumberJson fSettleOrgIDObj = new NumberJson();
        fSettleOrgIDObj.setFNumber(fsettleOrgId);

        NumberJson fSettleCurrIdObj = new NumberJson();
        fSettleCurrIdObj.setFNumber(fsettleCurrId);

        saleInSubHeadEntityJson.setFSettleOrgID(fSettleOrgIDObj);
        saleInSubHeadEntityJson.setFSettleCurrId(fSettleCurrIdObj);
        saleInSubHeadEntityJson.setFThirdBillNo(fthirdBillNo);

        salInModelJson.setSubHeadEntity(saleInSubHeadEntityJson);


        List<ReSaleFEntity> fentitylist = dto.getFentitylist();

        List<SalInFEntityJson> FEntity = new ArrayList<>();

        for (ReSaleFEntity reSaleFEntity : fentitylist) {
            SalInFEntityJson salInFEntityJson = new SalInFEntityJson();

            salInFEntityJson.setFEntryID("0");

            NumberJson fMaterialIdJson = new NumberJson();
            fMaterialIdJson.setFNumber(reSaleFEntity.getFmaterialId());
            salInFEntityJson.setFMaterialId(fMaterialIdJson);

            String fRealQty = reSaleFEntity.getFrealqty();
            if (StrUtil.isNotEmpty(fRealQty)) {
                salInFEntityJson.setFRealQty(fRealQty);
            } else {
                salInFEntityJson.setFRealQty("");
            }

            NumberJson fStockIdJson = new NumberJson();
            fStockIdJson.setFNumber(reSaleFEntity.getFstockid());
            salInFEntityJson.setFStockId(fStockIdJson);

            String fTaxPrice = reSaleFEntity.getFtaxprice();
            if (StrUtil.isNotEmpty(fTaxPrice)) {
                salInFEntityJson.setFTaxPrice(fTaxPrice);
            } else {
                salInFEntityJson.setFTaxPrice("");
            }

            Integer fEntryTaxRate = reSaleFEntity.getFentrytaxrate();
            salInFEntityJson.setFEntryTaxRate(fEntryTaxRate);



            String fOrderNo = reSaleFEntity.getForderno();
            if (StrUtil.isNotEmpty(fOrderNo)) {
                salInFEntityJson.setFOrderNo(fOrderNo);
            } else {
                salInFEntityJson.setFOrderNo("");
            }


            String fDsgSrcoid1 = reSaleFEntity.getFdsgsrcoid1();
            if (StrUtil.isNotEmpty(fDsgSrcoid1)) {
                salInFEntityJson.setF_DSG_srcoid1(fDsgSrcoid1);
            } else {
                salInFEntityJson.setF_DSG_srcoid1("");
            }

            //默认批次效期、生产日期、效期
            NumberJson flotJson = new NumberJson();
            flotJson.setFNumber(k3Config.getFlot());
            salInFEntityJson.setFLot(flotJson);
            salInFEntityJson.setFExpiryDate(k3Config.getFExpiryDate());
            salInFEntityJson.setFProduceDate(k3Config.getFProduceDate());

            FEntity.add(salInFEntityJson);
        }

        salInModelJson.setFEntity(FEntity);

        salInJson.setModel(salInModelJson);
        Gson gson = new Gson();
        String json = gson.toJson(salInJson);
        log.info("json=" + json);


        return json;
    }

    @Override
    public String getSavePurchaseJsons(PutPurReqDto dto) {

        String fbillno = dto.getFbillno();
        String fdate = dto.getFdate();
        String fstockorgid = dto.getFstockorgid();
        String fpurchaseorgid = dto.getFpurchaseorgid();
        String fsupplierId = dto.getFsupplierId();
        String fthirdbillno = dto.getFthirdbillno();
        String fdemandorgid = dto.getFdemandorgid();
        List<PurFEntry> fentrylist = dto.getFentrylist();

        PurchaseJson purchaseJson = new PurchaseJson();
        purchaseJson.setIsDeleteEntry("true");
        purchaseJson.setSubSystemId("21");
        purchaseJson.setIsVerifyBaseDataField("true");
        purchaseJson.setIsAutoAdjustField("true");
        //测试用false
        purchaseJson.setIsAutoSubmitAndAudit("true");
        purchaseJson.setIsEntryBatchFill("True");
        purchaseJson.setInterationFlags("STK_InvCheckDetailMessage");

        PurchaseModelJson purchaseModelJson = new PurchaseModelJson();

        NumberJson fBillTypeIDObj = new NumberJson();
        String fBillTypeId = "RKD01_SYS";
        fBillTypeIDObj.setFNumber(fBillTypeId);
        purchaseModelJson.setFBillTypeID(fBillTypeIDObj);

        if(StrUtil.isNotEmpty(fbillno)){
            purchaseModelJson.setFBillNo(fbillno);
        }else {
            purchaseModelJson.setFBillNo("");
        }

        if(StrUtil.isNotEmpty(fdate)){
            purchaseModelJson.setFDate(fdate);
        }else {
            purchaseModelJson.setFDate("");
        }

        FThirdBillNoJson fInStockFinObj = new FThirdBillNoJson();
        if(StrUtil.isNotEmpty(fthirdbillno)){
            fInStockFinObj.setFTHIRDBILLNO(fthirdbillno);
        }else {
            fInStockFinObj.setFTHIRDBILLNO("");
        }

        NumberJson fSettleCurrIDJson = new NumberJson();
        fSettleCurrIDJson.setFNumber(dto.getFsettlecurrid());

        fInStockFinObj.setFSettleCurrId(fSettleCurrIDJson);

        String fisincludedtax = dto.getFisincludedtax();
        if(fisincludedtax == null){
            fInStockFinObj.setFIsIncludedTax("false");
        }else {
            fInStockFinObj.setFIsIncludedTax("true");
        }

        String falldiscount = dto.getFalldiscount();
        if(falldiscount != null){
            fInStockFinObj.setFAllDisCount(falldiscount);
        }

        purchaseModelJson.setFInStockFin(fInStockFinObj);

        NumberJson fStockOrgIdObj = new NumberJson();
        if(StrUtil.isNotEmpty(fstockorgid)){
            fStockOrgIdObj.setFNumber(fstockorgid);
        }else {
            fStockOrgIdObj.setFNumber("");
        }
        purchaseModelJson.setFStockOrgId(fStockOrgIdObj);


        NumberJson fPurchaseOrgIdObj = new NumberJson();
        if (StrUtil.isNotEmpty(fpurchaseorgid)){
            fPurchaseOrgIdObj.setFNumber(fpurchaseorgid);
        }else {
            fPurchaseOrgIdObj.setFNumber("");
        }
        purchaseModelJson.setFPurchaseOrgId(fPurchaseOrgIdObj);

        NumberJson fSupplierIdObj = new NumberJson();
        if (StrUtil.isNotEmpty(fsupplierId)){
            fSupplierIdObj.setFNumber(fsupplierId);
        }else {
            fSupplierIdObj.setFNumber("");
        }
        purchaseModelJson.setFSupplierId(fSupplierIdObj);

        NumberJson fDemandOrgIdObj = new NumberJson();
        if (StrUtil.isNotEmpty(fdemandorgid)){
            fDemandOrgIdObj.setFNumber(fdemandorgid);
        }else {
            fDemandOrgIdObj.setFNumber("");
        }
        purchaseModelJson.setFDemandOrgId(fDemandOrgIdObj);


        List<FInStockEntryJson> FInStockEntry = new ArrayList();


        for(PurFEntry purFEntry : fentrylist){
            FInStockEntryJson fInStockEntryJson = new FInStockEntryJson();

            fInStockEntryJson.setFEntryID("0");

            NumberJson fMaterialIdObj = new NumberJson();

            String fMaterialId = purFEntry.getFmaterialId();
            if(StrUtil.isNotEmpty(fMaterialId)){
                fMaterialIdObj.setFNumber(fMaterialId);
            }else {
                fMaterialIdObj.setFNumber("");
            }
            fInStockEntryJson.setFMaterialId(fMaterialIdObj);

            String fRealQty = purFEntry.getFrealqty();
            if(StrUtil.isNotEmpty(fRealQty)){
                fInStockEntryJson.setFRealQty(fRealQty);
            }else {
                fInStockEntryJson.setFRealQty("");
            }

            String fStockId = purFEntry.getFstockid();
            NumberJson fStockIdObj = new NumberJson();
            if(StrUtil.isNotEmpty(fStockId)){
                fStockIdObj.setFNumber(fStockId);
            }else {
                fStockIdObj.setFNumber("");
            }
            fInStockEntryJson.setFStockId(fStockIdObj);



            String fTaxPrice = purFEntry.getFtaxprice();
            if(StrUtil.isNotEmpty(fTaxPrice)){
                fInStockEntryJson.setFTAXPRICE(fTaxPrice);
            }else {
                fInStockEntryJson.setFTAXPRICE("");
            }

            String fNote = "";
            if(StrUtil.isNotEmpty(fNote)){
                fInStockEntryJson.setFNOTE(fTaxPrice);
            }else {
                fInStockEntryJson.setFNOTE("");
            }



            String fEntryTaxRate = purFEntry.getFentrytaxrate();
            if(StrUtil.isNotEmpty(fEntryTaxRate)){
                fInStockEntryJson.setFENTRYTAXRATE(fEntryTaxRate);
            }else {
                fInStockEntryJson.setFENTRYTAXRATE("");
            }

            //默认批次效期、生产日期、效期
            NumberJson flotJson = new NumberJson();
            flotJson.setFNumber(k3Config.getFlot());
            fInStockEntryJson.setFLot(flotJson);
            fInStockEntryJson.setFExpiryDate(k3Config.getFExpiryDate());
            fInStockEntryJson.setFProduceDate(k3Config.getFProduceDate());

            FInStockEntry.add(fInStockEntryJson);
        }
        purchaseModelJson.setFInStockEntry(FInStockEntry);

        purchaseJson.setModel(purchaseModelJson);
        Gson gson = new Gson();
        String param = gson.toJson(purchaseJson);

        return param;
    }

    @Override
    public String getSaveRetPurJsons(GetPutRePurReqDto dto) {


        String fbillno = dto.getFbillno();
        String fstockorgid = dto.getFstockorgid();
        String fpurchaseorgid = dto.getFpurchaseorgid();
        String fsupplierid = dto.getFsupplierid();
        String fdemandorgid = dto.getFdemandorgid();
        String fsettlecurrid = dto.getFsettlecurrid();
        String fthirdbillno = dto.getFthirdbillno();
        String fdate = dto.getFdate();


        List<RePurFentity> fentrylist = dto.getFentitylist();

        RetPurJson retPurJson = new RetPurJson();
        retPurJson.setCreator("demo");
        retPurJson.setIsDeleteEntry("True");
        retPurJson.setSubSystemId("21");
        retPurJson.setIsVerifyBaseDataField("true");
        //测试用false
        retPurJson.setIsAutoSubmitAndAudit("true");
        retPurJson.setIsEntryBatchFill("True");
        retPurJson.setInterationFlags("STK_InvCheckDetailMessage");
        retPurJson.setIsAutoAdjustField("true");

        RePurModelJson rePurModelJson = new RePurModelJson();

        NumberJson FBillTypeID = new NumberJson();
        FBillTypeID.setFNumber("TLD01_SYS");
        rePurModelJson.setFBillTypeID(FBillTypeID);

        rePurModelJson.setFBillNo(fbillno);
        rePurModelJson.setFDate(fdate);

        NumberJson FStockOrgId = new NumberJson();
        FStockOrgId.setFNumber(fstockorgid);
        rePurModelJson.setFStockOrgId(FStockOrgId);


        NumberJson FPurchaseOrgId = new NumberJson();
        FPurchaseOrgId.setFNumber(fpurchaseorgid);
        rePurModelJson.setFPurchaseOrgId(FPurchaseOrgId);

        NumberJson FSupplierId = new NumberJson();
        FSupplierId.setFNumber(fsupplierid);
        rePurModelJson.setFSupplierId(FSupplierId);

        NumberJson FDemandOrgId = new NumberJson();
        FDemandOrgId.setFNumber(fdemandorgid);

        FPURMRBFIN FPURMRBFIN = new FPURMRBFIN();
        FPURMRBFIN.setFTHIRDBILLNO(fthirdbillno);

        NumberJson FSettleCurrId = new NumberJson();
        FSettleCurrId.setFNumber(fsettlecurrid);
        FPURMRBFIN.setFSettleCurrId(FSettleCurrId);

        rePurModelJson.setFPURMRBFIN(FPURMRBFIN);

        List<FPURMRB> FPURMRBENTRY = rePurModelJson.getFPURMRBENTRY();

        List<RePurFentity> fentitylist = dto.getFentitylist();


        for (RePurFentity rePurFentity : fentitylist){
            FPURMRB FPURMRB = new FPURMRB();

            FPURMRB.setFEntryID("0");

            NumberJson FMaterialId = new NumberJson();
            FMaterialId.setFNumber(rePurFentity.getFmaterialid());
            FPURMRB.setFMaterialId(FMaterialId);

            FPURMRB.setFRMREALQTY(rePurFentity.getFrmrealqty());

            NumberJson FStockId = new NumberJson();
            FStockId.setFNumber(rePurFentity.getFstockid());
            FPURMRB.setFStockId(FStockId);

            FPURMRB.setFTAXPRICE(rePurFentity.getFtaxprice());
            FPURMRB.setFnote("");
            FPURMRB.setFENTRYTAXRATE(rePurFentity.getFentrytaxrate());

            //默认批次效期、生产日期、效期
            NumberJson flotJson = new NumberJson();
            flotJson.setFNumber(k3Config.getFlot());
            FPURMRB.setFLot(flotJson);
            FPURMRB.setFExpiryDate(k3Config.getFExpiryDate());
            FPURMRB.setFProduceDate(k3Config.getFProduceDate());

            FPURMRBENTRY.add(FPURMRB);
        }

        retPurJson.setModel(rePurModelJson);

        Gson gson = new Gson();

        String param = gson.toJson(retPurJson);
        return param;
    }


    @Override
    public String getSaveTrfJson(PutTrfReqDto dto) {

        TrfJson trfJson = new TrfJson();

        trfJson.setIsDeleteEntry("True");
        trfJson.setSubSystemId("");
        trfJson.setIsVerifyBaseDataField("false");
        trfJson.setIsEntryBatchFill("true");
        trfJson.setValidateFlag("true");
        trfJson.setNumberSearch("true");
        trfJson.setIsAutoAdjustField("false");
        trfJson.setInterationFlags("STK_InvCheckDetailMessage");
        trfJson.setIgnoreInterationFlag("");
        trfJson.setIsControlPrecision("false");
        trfJson.setValidateRepeatJson("false");
        trfJson.setIsAutoSubmitAndAudit("true");

        TrfModelJson modelJson = new TrfModelJson();

        modelJson.setFID("0");
        modelJson.setFBillNo(dto.getFbillNo());
        NumberJson fBillTypeIDObj = new NumberJson();
        fBillTypeIDObj.setFNumber("ZJDB01_SYS");
        modelJson.setFBillTypeID(fBillTypeIDObj);

        modelJson.setFBizType("NORMAL");
        modelJson.setFTransferDirect("GENERAL");
        modelJson.setFTransferBizType("InnerOrgTransfer");

        NumberJson fStockOutOrgIdObj = new NumberJson();
        fStockOutOrgIdObj.setFNumber(dto.getFstockoutorgid());

        modelJson.setFStockOutOrgId(fStockOutOrgIdObj);

        modelJson.setFDate(dto.getFdate());

        modelJson.setFThirdSrcBillNo(dto.getFthirdsrcbillno());

        List<FBillEntryJson> fBillEntry = modelJson.getFBillEntry();

        List<TrfFEntity> trfFEntities = dto.getFentitylist();
        for(TrfFEntity trfFEntity : trfFEntities){
            FBillEntryJson fBillEntryJson = new FBillEntryJson();
            fBillEntryJson.setFRowType("Standard");

            NumberJson fMaterialIdObj = new NumberJson();
            fMaterialIdObj.setFNumber(trfFEntity.getFmaterialid());
            fBillEntryJson.setFMaterialId(fMaterialIdObj);

            NumberJson fSrcStockIdObj = new NumberJson();
            fSrcStockIdObj.setFNumber(trfFEntity.getFsrcstockid());
            fBillEntryJson.setFSrcStockId(fSrcStockIdObj);

            NumberJson fDestStockIdObj = new NumberJson();
            fDestStockIdObj.setFNumber(trfFEntity.getFdeststockid());
            fBillEntryJson.setFDestStockId(fDestStockIdObj);

            NumberJson flotJson = new NumberJson();
            flotJson.setFNumber(k3Config.getFlot());
            fBillEntryJson.setFlot(flotJson);
            fBillEntryJson.setFExpiryDate(k3Config.getFExpiryDate());
            fBillEntryJson.setFProduceDate(k3Config.getFProduceDate());

            fBillEntryJson.setFQty(trfFEntity.getFqty());

            fBillEntry.add(fBillEntryJson);

        }

        trfJson.setModel(modelJson);

        Gson gson = new Gson();

        String param = gson.toJson(trfJson);

        return param;
    }

    @Override
    public String getSaveAssyJson(PutAssemblyDto dto) {
        AssyJson assyJson = new AssyJson();

        assyJson.setIsDeleteEntry("true");
        assyJson.setSubSystemId("");
        assyJson.setIsVerifyBaseDataField("false");
        assyJson.setIsEntryBatchFill("true");
        assyJson.setValidateFlag("true");
        assyJson.setNumberSearch("true");
        assyJson.setIsAutoAdjustField("true");
        assyJson.setInterationFlags("");
        assyJson.setInterationFlags("");
        assyJson.setIsAutoSubmitAndAudit("true");
        assyJson.setIsControlPrecision("false");
        assyJson.setValidateRepeatJson("false");

        AssyModelJson modelJson = new AssyModelJson();

        modelJson.setFID("0");
        modelJson.setFBillNo(dto.getFillno());

        NumberJson fStockOrgId = new NumberJson();
        fStockOrgId.setFNumber(dto.getFstockorgid());
        modelJson.setFStockOrgId(fStockOrgId);

        modelJson.setFAffairType(dto.getFaffairtype());
        modelJson.setFDate(dto.getFdate());

        List<AssyFEntity> assyFEntities = dto.getAssyFEntities();

        List<AssyFEntityJson> FEntity = new ArrayList<>();


        for (AssyFEntity assyFEntity : assyFEntities){

            AssyFEntityJson fEntity = new AssyFEntityJson();


            String fmaterialid = assyFEntity.getFmaterialid();
            String fstockid = assyFEntity.getFstockid();
            String fqty = assyFEntity.getFqty();
            String frefbomid = assyFEntity.getFrefbomid();

            NumberJson FMaterialID = new NumberJson();
            FMaterialID.setFNumber(fmaterialid);
            fEntity.setFMaterialID(FMaterialID);

            fEntity.setFQty(fqty);

            NumberJson FStockID = new NumberJson();
            FStockID.setFNumber(assyFEntity.getFstockid());
            fEntity.setFStockID(FStockID);


            NumberJson FRefBomID = new NumberJson();
            FRefBomID.setFNumber(frefbomid);
            fEntity.setFRefBomID(FRefBomID);

            fEntity.setFCPWdtID(dto.getFillno());
            fEntity.setFSrcBillNo(dto.getFillno());
            fEntity.setFee_ETY("0");

            NumberJson flotJson = new NumberJson();
            flotJson.setFNumber(k3Config.getFlot());
            fEntity.setFLot(flotJson);
            fEntity.setFExpiryDate(k3Config.getFExpiryDate());
            fEntity.setFProduceDate(k3Config.getFProduceDate());


            List<AssyFSubEntity> assyFSubEntities  = assyFEntity.getAssyFSubEntities();


            List<AssyFsubFntityJson> FSubEntity = new ArrayList<>();

            for (AssyFSubEntity assyFSubEntity : assyFSubEntities){

                AssyFsubFntityJson assyFsubFntityJson = new AssyFsubFntityJson();

                NumberJson FMaterialIDSETY = new NumberJson();
                FMaterialIDSETY.setFNumber(assyFSubEntity.getFmaterialdsety());
                assyFsubFntityJson.setFMaterialIDSETY(FMaterialIDSETY);

                NumberJson FStockIDSETY = new NumberJson();
                FStockIDSETY.setFNumber(assyFSubEntity.getFstockidsety());
                assyFsubFntityJson.setFStockIDSETY(FStockIDSETY);

                assyFsubFntityJson.setFQtySETY(assyFSubEntity.getFqtysety());

                assyFsubFntityJson.setFZJWdtID("");
                assyFsubFntityJson.setFCostProportion("");

                //默认批次效期、生产日期、效期
                assyFsubFntityJson.setFLOTSETY(flotJson);
                assyFsubFntityJson.setFEXPIRYDATESETY(k3Config.getFExpiryDate());
                assyFsubFntityJson.setFProduceDateSETY(k3Config.getFProduceDate());


                FSubEntity.add(assyFsubFntityJson);
            }
            fEntity.setFSubEntity(FSubEntity);

            FEntity.add(fEntity);
        }

        modelJson.setFEntity(FEntity);


        assyJson.setModel(modelJson);


        Gson gson = new Gson();

        String param = gson.toJson(assyJson);


        return param;
    }
}
