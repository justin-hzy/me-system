package com.me.modules.token.service.impl;

import com.alibaba.fastjson.JSON;
import com.me.modules.sys.http.HttpService;
import com.me.modules.token.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {


    private HttpService httpService;

    @Override
    public String queryToken() {
        /*String baseUrl = "https://cbs8-openapi-reprd.csuat.cmburl.cn/openapi/app/v1/app/token";*/

        String baseUrl = "https://tmcapi.cmbchina.com/openapi/app/v1/app/token";

        Map<String,String> header = new HashMap<>();

        Map<String,String> body = new HashMap<>();
        /*body.put("app_id","fNVMWxuR");
        body.put("app_secret","6eb5da3f18bd5684090147ddf6dc80e19f8a4892");
        body.put("grant_type","client_credentials");*/

        body.put("app_id","Z4xMAuZg");
        body.put("app_secret","d90437e94061ee07830464b61b400782ae379fb2");
        body.put("grant_type","client_credentials");

        String response = httpService.sendPostRequest(baseUrl,header,body);
        String token = JSON.parseObject(response).getJSONObject("data").getString("token");
        return token;
    }
}
