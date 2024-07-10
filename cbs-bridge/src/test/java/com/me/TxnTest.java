package com.me;

import com.alibaba.fastjson.JSONObject;
import com.me.common.enums.Constants;
import com.me.common.utils.SM2Util;
import com.me.modules.transaction.dto.QryTxnReqDto;
import com.me.modules.transaction.service.TxnService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@SpringBootTest
@Slf4j
public class TxnTest {

    /**
     * 财资管理云公钥(平台公钥)
     */
    /*测试 046CA4F6B9C5771601CF418C7BF871EB43984C84C39AEBC527A56B1AC384571887F2057C1E964B545D27C57FA463924BF6F6A8A1C60C172C362A1CAA21519A8FBB*/
    /*生产 04CD09E422766D2412A9BBF802D2185C548412EF9CC20D47357D2975DFEB9CBD5270FA3F17F261B77A471C1800F7391407134C2629FEA3EDD58F67340914F19723*/
    static final String bodyEncryptionKey = "046CA4F6B9C5771601CF418C7BF871EB43984C84C39AEBC527A56B1AC384571887F2057C1E964B545D27C57FA463924BF6F6A8A1C60C172C362A1CAA21519A8FBB";



    /**
     * 企业私钥（加密）
     */
    /*测试 347bf1f723af15f31de13819e0e28025927e080e69060afa5d7f7097ef2edc55*/
    /*生产 96c0ef256c22c15ff6c5f70669b3a8d6ec1352cb490ecade7c9b3c386cbf22b8*/
    static final String signEncryptionPrivateKey = "347bf1f723af15f31de13819e0e28025927e080e69060afa5d7f7097ef2edc55";

    /**
     * 企业私钥（解密）
     */
    /*测试 347bf1f723af15f31de13819e0e28025927e080e69060afa5d7f7097ef2edc55*/
    /*生产 96c0ef256c22c15ff6c5f70669b3a8d6ec1352cb490ecade7c9b3c386cbf22b8*/
    static final String bodyDecryptionKey = "347bf1f723af15f31de13819e0e28025927e080e69060afa5d7f7097ef2edc55";

    /**
     * 根据appid和appsecert获取的token
     */
    static final String token = "14078aab-fa23-4cd1-a1ce-f98c698612b3";//chongxin

    /**
     * 接口路径
     */
    //static final String TARGET_URL = "http://cbs8-gateway-openapi-dev.paas.cmbchina.cn/openapi/account/accounts-current-balance/erp/query";
    /*
    测试 https://cbs8-openapi-reprd.csuat.cmburl.cn
    生产 https://tmcapi.cmbchina.com
    */
    static final String TARGET_URL = "https://cbs8-openapi-reprd.csuat.cmburl.cn/openapi/account/openapi/v1/transaction-detail/query";

//    static final String TARGET_URL = "https://tmcapi.cmbchina.com/openapi/account/openapi/v1/account/query";

    @Autowired
    TxnService txnService;

    @Test
    public void queryTmrTxn() throws Exception {
        /*QryTxnReqDto qryTxnReqDto = new QryTxnReqDto();
        qryTxnReqDto.setDateType("0");
        qryTxnReqDto.setStartDate("2024-01-11");
        qryTxnReqDto.setEndDate("2024-01-15");
        String requestData = JSONObject.toJSONString(qryTxnReqDto);

        CloseableHttpClient client = HttpClients.custom()
                // 禁止HttpClient自动解压缩
                .disableContentCompression()
                .build();

        HttpPost httpPost = setupRequest(requestData);

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            byte[] finalResponseData = handleResponse(response);
            log.info("\n返回结果：{}", new String(finalResponseData));
        }catch (IOException ignored){
            throw new IOException("网络连接失败或超时！");
        }finally {
            client.close();
        }*/


        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yesterdayString = yesterday.format(formatter);


        Integer currentPage = 1;
        Map<String,Integer> respMap = txnService.queryTxn(currentPage,yesterdayString);
        Integer nextPage = respMap.get("nextPage");

        while(nextPage !=0){
            respMap = txnService.queryTxn(nextPage,yesterdayString);
            nextPage = respMap.get("nextPage");
            log.info("nextPage="+nextPage);
        }
    }

    @Test
    public void syncHistory() throws IOException {

        String historydayString = "2024-05-28";

        Integer currentPage = 1;
        Map<String,Integer> respMap = txnService.queryTxn(currentPage,historydayString);
        Integer nextPage = respMap.get("nextPage");

        while(nextPage !=0){
            respMap = txnService.queryTxn(nextPage,historydayString);
            nextPage = respMap.get("nextPage");
            log.info("nextPage="+nextPage);
        }
        log.info("getTmrTxnJob执行结束");
    }

    @Test
    public void queryTodayTxn() throws Exception {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayString = today.format(formatter);

        Integer currentPage = 1;
        Map<String,Integer> respMap = txnService.queryTxn(currentPage,todayString);
        Integer nextPage = respMap.get("nextPage");

        while(nextPage !=0){
            respMap = txnService.queryTxn(nextPage,todayString);
            nextPage = respMap.get("nextPage");
            log.info("nextPage="+nextPage);
        }
    }

    private static HttpPost setupRequest(String requestData) {
        long timestamp = System.currentTimeMillis();

        // 请求数据拼接：  报文体+时间戳
        byte[] requestDataBytes = requestData.getBytes(StandardCharsets.UTF_8);
        byte[] timestampBytes = ("&timestamp=" + timestamp).getBytes(StandardCharsets.UTF_8);
        byte[] newBytes = new byte[requestDataBytes.length + timestampBytes.length];
        System.arraycopy(requestDataBytes, 0, newBytes, 0, requestDataBytes.length);
        System.arraycopy(timestampBytes, 0, newBytes, requestDataBytes.length, timestampBytes.length);

        // 生成签名
        byte[] signature = SM2Util.sign(signEncryptionPrivateKey, newBytes);
        String sign = Base64.encodeBase64String(SM2Util.encodeDERSignature(signature));
        log.info("签名:{}", sign);

        // 设置请求URL
        HttpPost httpPost = new HttpPost(TARGET_URL);
        // 请求头设置签名
        httpPost.setHeader(Constants.SIGN_HEADER_NAME, sign);
        // 请求头设置时间戳
        httpPost.setHeader(Constants.TIMESTAMP_HEADER, Long.toString(timestamp));
        // 请求头设置请求参数格式，请根据实际情况改写
        httpPost.setHeader(HTTP.CONTENT_TYPE, Constants.TARGET_CONTENT_TYPE);
        // 请求头设置TOKEN
        httpPost.setHeader(Constants.AUTHORIZATION, Constants.BEARER + token);

        // 报文体加密
        byte[] encryptedData = SM2Util.encrypt(bodyEncryptionKey, requestDataBytes);
        // 设置请求体
        httpPost.setEntity(new ByteArrayEntity(encryptedData));

        return httpPost;
    }

    private static byte[] handleResponse(HttpResponse response) throws Exception {
        InputStream content = response.getEntity().getContent();
        byte[] responseData = IOUtils.toByteArray(content);

        if (responseData == null || responseData.length == 0) {
            return responseData == null ? new byte[0] : responseData;
        }

        // 步骤1 原始响应报文解密 如果服务网关获取加解密密钥失败，则无法解密请求报文，且无法加密响应报文。 这时候，网关会直接返回错误信息，响应报文是未加密状态。
        Boolean encryptionEnable = getHeader(response, Constants.ENCRYPTION_ENABLED_HEADER_NAME);

        if (Boolean.TRUE.equals(encryptionEnable)) {
            responseData = SM2Util.decrypt(bodyDecryptionKey, responseData);
        }

        Boolean xMbcloudCompress = getHeader(response, Constants.X_MBCLOUD_COMPRESS);
        if (Boolean.TRUE.equals(xMbcloudCompress)) {
            responseData = decompress(responseData);
        }

        return responseData;


    }

    private static Boolean getHeader(HttpMessage message, String name) {
        Header header = message.getFirstHeader(name);
        return header != null;
    }

    public static byte[] decompress(byte[] data) throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        GZIPInputStream gzipInput = new GZIPInputStream(input);
        return IOUtils.toByteArray(gzipInput);
    }
}
