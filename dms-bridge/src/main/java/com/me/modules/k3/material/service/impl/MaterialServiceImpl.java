package com.me.modules.k3.material.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.modules.k3.material.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    @Override
    public void queryMaterials() {
        //注意 1：此处不再使用参数形式传入用户名及密码等敏感信息，改为在登录配置文件中设置。
        //注意 2：必须先配置第三方系统登录授权信息后，再进行业务操作，详情参考各语言版本SDK介绍中的登录配置文件说明。
        //读取配置，初始化SDK
        K3CloudApi client = new K3CloudApi();
        //请求参数，要求为json字符串,拼装jsonObject
        Integer startRow = 0;
        JSONObject jsonObject0 = new JSONObject();
        jsonObject0.fluentPut("FormId","BD_MATERIAL");
        String fileKeys = "FNumber,FCreateOrgId.FNumber,FUseOrgId.FNumber,FName,FErpClsID,F_RLIZ_Base.FName,FSpecAttrCategoryID.FNumber,FSpecAttrCategoryID.FNumber,F_RLIZ_LSJ,F_CBJ ,Fcolor,FVOLUMEUNITID.FNumber,FDefaultVendor,FBaseUnitId.FNumber,FCategoryID.FNumber,FSpecification,FBARCODE,F_YJFZ.FNumber";
        jsonObject0.put("FieldKeys",fileKeys);
        jsonObject0.put("StartRow",startRow);
        jsonObject0.put("Limit",100);
        jsonObject0.put("TopRowCount",0);

        JSONArray FilterString = new JSONArray();

        log.info(jsonObject0.toJSONString());



        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("Left","(");
        jsonObject1.put("FieldName","FModifyDate");
        jsonObject1.put("Compare",">");
        jsonObject1.put("Value","2024-04-01 09:00:00");
        jsonObject1.put("Right",")");
        jsonObject1.put("Logic","AND");

        log.info("jsonObject1="+jsonObject1.toJSONString());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("Left","(");
        jsonObject2.put("FieldName","FModifyDate");
        jsonObject2.put("Compare","<");
        jsonObject2.put("Value","2024-04-01 23:59:59");
        jsonObject2.put("Right",")");
        jsonObject2.put("Logic","AND");

        log.info("jsonObject2="+jsonObject2.toJSONString());

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("Left","(");
        jsonObject3.put("FieldName","FDocumentStatus");
        jsonObject3.put("Compare","StatusEqualto");
        jsonObject3.put("Value","C");
        jsonObject3.put("Right",")");
        jsonObject3.put("Logic","AND");

        log.info("jsonObject3="+jsonObject3.toJSONString());

        FilterString.add(jsonObject1);
        FilterString.add(jsonObject2);
        FilterString.add(jsonObject3);

        jsonObject0.put("FilterString",FilterString);

        log.info("jsonObject0="+jsonObject0.toJSONString());

        String jsonData = jsonObject0.toJSONString();


        try {
            //调用接口
            String resultJson = String.valueOf(client.billQuery(jsonData));
            System.out.println("接口返回结果: " + resultJson);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
