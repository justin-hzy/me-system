package com.me.modules.rate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.rate.dto.QryRateReqDto;
import com.me.modules.rate.service.RateService;
import com.me.modules.sys.request.RequestService;
import com.me.modules.sys.response.ResponseService;
import com.me.modules.token.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class RateServiceImpl implements RateService {

    private TokenService tokenService;

    private RequestService requestService;

    private ResponseService responseService;

    static final String bodyEncryptionKey = "046CA4F6B9C5771601CF418C7BF871EB43984C84C39AEBC527A56B1AC384571887F2057C1E964B545D27C57FA463924BF6F6A8A1C60C172C362A1CAA21519A8FBB";

    static final String signEncryptionPrivateKey = "347bf1f723af15f31de13819e0e28025927e080e69060afa5d7f7097ef2edc55";

    static final String TARGET_URL = "https://cbs8-openapi-reprd.csuat.cmburl.cn/openapi/cfg/openapi/v1/exchange-rate/page-query";

    static final String bodyDecryptionKey = "347bf1f723af15f31de13819e0e28025927e080e69060afa5d7f7097ef2edc55";



    @Override
    public void queryRate() throws IOException {
        QryRateReqDto qryRateReqDto = new QryRateReqDto();

//        qryRateReqDto.setValidDateStart("2024-03-01");
//        qryRateReqDto.setValidDateEnd("2024-03-01");
        qryRateReqDto.setPageSize(50);
        String requestData = JSONObject.toJSONString(qryRateReqDto);

        //log.info("requestData="+requestData);

        CloseableHttpClient client = HttpClients.custom()
                // 禁止HttpClient自动解压缩
                .disableContentCompression()
                .build();

        String token = tokenService.queryToken();
        HttpPost httpPost = requestService.setupRequest(requestData,token,signEncryptionPrivateKey,TARGET_URL,bodyEncryptionKey);

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            byte[] finalResponseData = responseService.handleResponse(response,bodyDecryptionKey);
            String respJson = new String(finalResponseData);
            log.info("\n返回结果：{}", respJson);
        }catch (IOException ignored){
            throw new IOException("网络连接失败或超时！");
        }finally {
            client.close();
        }
    }
}
