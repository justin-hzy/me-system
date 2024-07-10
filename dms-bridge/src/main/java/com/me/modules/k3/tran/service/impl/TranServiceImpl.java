package com.me.modules.k3.tran.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;

import com.kingdee.bos.webapi.entity.IdentifyInfo;
import com.kingdee.bos.webapi.entity.RepoError;
import com.kingdee.bos.webapi.entity.RepoRet;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.common.config.K3Config;
import com.me.modules.k3.assembly.dto.PutAssemblyDto;
import com.me.modules.k3.inventory.service.UfInventoryService;
import com.me.modules.k3.json.service.JsonService;
import com.me.modules.k3.log.entity.DmsK3ErrorLog;
import com.me.modules.k3.log.service.DmsK3ErrorLogService;
import com.me.modules.k3.purchase.dto.PutPurReqDto;
import com.me.modules.k3.retpur.dto.GetPutRePurReqDto;
import com.me.modules.k3.retsale.dto.PutReSaleReqDto;
import com.me.modules.k3.retsale.service.K3RetSaleService;
import com.me.modules.k3.sale.dto.PutSaleReqDto;
import com.me.modules.k3.sale.entity.FlSaleDtlLog;
import com.me.modules.k3.sale.pojo.SaleFEntity;
import com.me.modules.k3.sale.service.FlSaleDtlLogService;
import com.me.modules.k3.tran.service.TranService;

import com.me.modules.k3.tranfer.dto.PutTrfReqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.fail;

@Service
@AllArgsConstructor
@Slf4j
public class TranServiceImpl implements TranService {


    private JsonService jsonService;

    private K3RetSaleService k3RetSaleService;

    private K3Config k3Config;

    private UfInventoryService ufInventoryService;

    private FlSaleDtlLogService flSaleDtlLogService;

    private DmsK3ErrorLogService errorLogService;

    @Override
    public String tranSaleOrder(PutSaleReqDto dto) throws Exception {


        String param = jsonService.getSaveSaleJsons(dto);


        //调用金蝶销售出库保存接口 没有测试环境以下代码暂时注释
        IdentifyInfo iden = new IdentifyInfo();
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());
        iden.setUserName(k3Config.getUserName());
        K3CloudApi client = new K3CloudApi(iden);


        //业务对象标识
        String formId = "SAL_OUTSTOCK";

        log.info("param="+param);
        //调用接口
        String resultJson = client.save(formId,param);

        //用于记录结果
        Gson gson = new Gson();
        RepoRet repoRet = gson.fromJson(resultJson, RepoRet.class);
        String result = gson.toJson(repoRet.getResult());
        if (repoRet.getResult().getResponseStatus().isIsSuccess()) {
            JSONObject resJson = new JSONObject();
            log.info("同步成功");
            log.info("result="+result);
            resJson.put("code",200);

            //加入或更新明细
            if("TW".equals(dto.getType())){
                insertSaleLog(dto);
            }

            return resJson.toJSONString();
        } else {
            log.info("接口返回结果: " + gson.toJson(repoRet.getResult().getResponseStatus()));
            JSONObject resJson = new JSONObject();
            resJson.put("code",500);
            saveErrorLog(repoRet.getResult().getResponseStatus().getErrors(),dto.getFbillno());

            return resJson.toJSONString();
        }
    }



    @Override
    public void tranSaleReOrder(PutReSaleReqDto dto) throws Exception {

        String param = jsonService.getSaveReSalJsons(dto);

        //业务对象标识
        String formId = "SAL_RETURNSTOCK";

        //调用金蝶销售出库保存接口 没有测试环境以下代码暂时注释
        //调用金蝶销售出库保存接口 没有测试环境以下代码暂时注释
        IdentifyInfo iden = new IdentifyInfo();
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());
        iden.setUserName(k3Config.getUserName());
        K3CloudApi client = new K3CloudApi(iden);

        //调用接口
        String resultJson = client.save(formId,param);
//
//            //用于记录结果
        Gson gson = new Gson();

        //对返回结果进行解析和校验

        RepoRet repoRet = gson.fromJson(resultJson, RepoRet.class);
        if (repoRet.getResult().getResponseStatus().isIsSuccess()) {
            System.out.printf("接口返回结果: %s%n", gson.toJson(repoRet.getResult()));
            log.info("同步成功");
        } else {
            Assert.fail("接口返回结果: " + gson.toJson(repoRet.getResult().getResponseStatus()));
        }
    }

    @Override
    public String tranPurchase(PutPurReqDto dto) throws Exception {

        String param = jsonService.getSavePurchaseJsons(dto);


        log.info("param="+param);

        //业务对象标识
        String formId = "STK_InStock";

        //调用金蝶销售出库保存接口 没有测试环境以下代码暂时注释
        IdentifyInfo iden = new IdentifyInfo();
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());
        iden.setUserName(k3Config.getUserName());
        K3CloudApi client = new K3CloudApi(iden);

        //调用接口
        String resultJson = client.save(formId,param);

        //用于记录结果
        Gson gson = new Gson();
        RepoRet repoRet = gson.fromJson(resultJson, RepoRet.class);
        String result = gson.toJson(repoRet.getResult());
        //log.info(result);
        if (repoRet.getResult().getResponseStatus().isIsSuccess()) {
            System.out.printf("接口返回结果: %s%n", gson.toJson(repoRet.getResult()));
            //更新主表状态
            JSONObject resJson = new JSONObject();
            log.info("同步成功");
            log.info("result="+result);
            resJson.put("code",200);
            return resJson.toJSONString();

            //更新中间表


        } else {
            List<RepoError> errors = repoRet.getResult().getResponseStatus().getErrors();
            //更新主表状态
            JSONObject resJson = new JSONObject();
            resJson.put("code",500);

            saveErrorLog(errors,dto.getFbillno());

            log.info("接口返回结果: " + gson.toJson(repoRet.getResult().getResponseStatus()));

            return resJson.toJSONString();
        }

    }

    @Override
    public String tranRetPur(GetPutRePurReqDto dto) throws Exception {

        String param = jsonService.getSaveRetPurJsons(dto);
        //业务对象标识
        String formId = "PUR_MRB";

        //调用金蝶销售出库保存接口 没有测试环境以下代码暂时注释
        //调用金蝶销售出库保存接口 没有测试环境以下代码暂时注释
        IdentifyInfo iden = new IdentifyInfo();
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());
        iden.setUserName(k3Config.getUserName());
        K3CloudApi client = new K3CloudApi(iden);

        log.info("param="+param);

        //调用接口
        String resultJson = client.save(formId,param);

        //用于记录结果
        Gson gson = new Gson();

        //对返回结果进行解析和校验

        RepoRet repoRet = gson.fromJson(resultJson, RepoRet.class);
        if (repoRet.getResult().getResponseStatus().isIsSuccess()) {
            System.out.printf("接口返回结果: %s%n", gson.toJson(repoRet.getResult()));
            JSONObject resJson = new JSONObject();
            log.info("同步成功");
            log.info("repoRet"+repoRet);
            resJson.put("code",200);
            return resJson.toJSONString();



        } else {
            Assert.fail("接口返回结果: " + gson.toJson(repoRet.getResult().getResponseStatus()));

            //更新主表状态
            JSONObject resJson = new JSONObject();
            resJson.put("code",500);

            saveErrorLog(repoRet.getResult().getResponseStatus().getErrors(),dto.getFbillno());
            return resJson.toJSONString();
        }
    }

    @Override
    public String tranTrf(PutTrfReqDto dto) throws Exception {

        String param = jsonService.getSaveTrfJson(dto);

        //调用金蝶销售出库保存接口 没有测试环境以下代码暂时注释
        IdentifyInfo iden = new IdentifyInfo();
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());
        iden.setUserName(k3Config.getUserName());
        K3CloudApi client = new K3CloudApi(iden);

        //业务对象标识
        String formId = "STK_TransferDirect";

        log.info("param="+param);
        //调用接口
        String resultJson = client.save(formId,param);

        //用于记录结果
        Gson gson = new Gson();
        RepoRet repoRet = gson.fromJson(resultJson, RepoRet.class);
        String result = gson.toJson(repoRet.getResult());

        log.info("result="+result);

        //用于记录结果
        if (repoRet.getResult().getResponseStatus().isIsSuccess()) {
            System.out.printf("接口返回结果: %s%n", gson.toJson(repoRet.getResult()));
            //更新主表状态
            JSONObject resJson = new JSONObject();
            log.info("同步成功");
            log.info("result="+result);
            resJson.put("code",200);
            return resJson.toJSONString();

            //更新中间表

        } else {
            Assert.fail("接口返回结果: " + gson.toJson(repoRet.getResult().getResponseStatus()));

            //更新主表状态
            JSONObject resJson = new JSONObject();
            resJson.put("code",500);

            saveErrorLog(repoRet.getResult().getResponseStatus().getErrors(),dto.getFbillNo());


            return resJson.toJSONString();
        }

    }

    @Override
    public String tranAssembly(PutAssemblyDto dto) throws Exception {
        String param = jsonService.getSaveAssyJson(dto);

        //业务对象标识
        String formId = "STK_AssembledApp";

        IdentifyInfo iden = new IdentifyInfo();
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());
        iden.setUserName(k3Config.getUserName());
        K3CloudApi client = new K3CloudApi(iden);

        log.info("param="+param);

        //调用接口
        String resultJson = client.save(formId,param);

        //用于记录结果
        Gson gson = new Gson();

        //对返回结果进行解析和校验

        RepoRet repoRet = gson.fromJson(resultJson, RepoRet.class);
        if (repoRet.getResult().getResponseStatus().isIsSuccess()) {
            System.out.printf("接口返回结果: %s%n", gson.toJson(repoRet.getResult()));
            JSONObject resJson = new JSONObject();
            log.info("同步成功");
            log.info("repoRet"+repoRet);
            resJson.put("code",200);
            return resJson.toJSONString();

        } else {
            Assert.fail("接口返回结果: " + gson.toJson(repoRet.getResult().getResponseStatus()));

            //更新主表状态
            JSONObject resJson = new JSONObject();
            resJson.put("code",500);

            saveErrorLog(repoRet.getResult().getResponseStatus().getErrors(),dto.getFillno());
            return resJson.toJSONString();
        }
    }


    public void insertSaleLog(PutSaleReqDto dto){

        String fbillno = dto.getFbillno();
        String fsaleorgid = dto.getFsaleorgid();

        List<SaleFEntity> fentitylist = dto.getFentitylist();
        List<FlSaleDtlLog> saleDtlLogs = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(new Date());


        QueryWrapper<FlSaleDtlLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("billNo",fbillno);
        List<FlSaleDtlLog > results = flSaleDtlLogService.list(queryWrapper);

        if (results.size() == 0){
            for (SaleFEntity saleFEntity : fentitylist){
                FlSaleDtlLog flSaleDtlLog = new FlSaleDtlLog();
                String fstockid = saleFEntity.getFstockid();
                String frealqty  = saleFEntity.getFrealqty();
                String sku = saleFEntity.getFmaterialId();

                flSaleDtlLog.setBillNo(fbillno);
                flSaleDtlLog.setSellorgid(fsaleorgid);
                flSaleDtlLog.setStockid(fstockid);
                flSaleDtlLog.setQuantity(frealqty);
                flSaleDtlLog.setSku(sku);
                flSaleDtlLog.setCreateTime(now);
                saleDtlLogs.add(flSaleDtlLog);
            }
            flSaleDtlLogService.saveBatch(saleDtlLogs);

            //更新中间表
            ufInventoryService.updateUfInventory(dto);
        }

    }

    private void saveErrorLog(List<RepoError> errors,String billNo){

        DmsK3ErrorLog errorLog = new DmsK3ErrorLog();


        String message = "";
        for (RepoError error : errors){
            message = message + error.getMessage()+",";
        }

        message = message.substring(0,message.length()-1);

        message.replace("\"","");

        errorLog.setMessage(message);
        errorLog.setBillNo(billNo);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(new Date());


        StringTokenizer tokenizer = new StringTokenizer(now, " ");

        String dateString = tokenizer.nextToken();
        String timeString = tokenizer.nextToken();

        errorLog.setCreateTime(now);
        errorLog.setTime(timeString);
        errorLog.setDate(dateString);

        errorLogService.save(errorLog);

    }



}
