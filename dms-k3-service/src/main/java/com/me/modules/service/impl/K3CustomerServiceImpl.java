package com.me.modules.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.modules.service.K3CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

import static org.junit.Assert.fail;

@Service
@Slf4j
public class K3CustomerServiceImpl implements K3CustomerService {

    @Override
    public void queryCustomers() throws IOException {
        //注意 1：此处不再使用参数形式传入用户名及密码等敏感信息，改为在登录配置文件中设置。
        //注意 2：必须先配置第三方系统登录授权信息后，再进行业务操作，详情参考各语言版本SDK介绍中的登录配置文件说明。
        //读取配置，初始化SDK
        K3CloudApi client = new K3CloudApi();
        //请求参数，要求为json字符串,拼装jsonObject
        Integer startRow = 0;
        JSONObject jsonObject0 = new JSONObject();
        jsonObject0.fluentPut("FormId","BD_Customer");
        String fileKeys = "FNumber,FUseOrgId,FUseOrgId,FName";
        jsonObject0.put("FieldKeys",fileKeys);
        jsonObject0.put("StartRow",startRow);
        jsonObject0.put("Limit",100);
        jsonObject0.put("TopRowCount",0);

        JSONArray FilterString = new JSONArray();

        log.info(jsonObject0.toJSONString());

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("Left","(");
        jsonObject3.put("FieldName","FDocumentStatus");
        jsonObject3.put("Compare","StatusEqualto");
        jsonObject3.put("Value","C");
        jsonObject3.put("Right",")");
        jsonObject3.put("Logic","AND");
//
        log.info("jsonObject3="+jsonObject3.toJSONString());
//
//        /*FilterString.add(jsonObject1);
//        FilterString.add(jsonObject2);*/
        FilterString.add(jsonObject3);
//
        jsonObject0.put("FilterString",FilterString);

        log.info("jsonObject0="+jsonObject0.toJSONString());

        String jsonData = jsonObject0.toJSONString();
//
//
        try {
            //调用接口
            String resultJson = String.valueOf(client.billQuery(jsonData));
            System.out.println("接口返回结果: " + resultJson);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
