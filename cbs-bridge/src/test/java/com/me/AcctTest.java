package com.me;

import com.alibaba.fastjson.JSONObject;
import com.me.common.enums.Constants;
import com.me.common.utils.SM2Util;
import com.me.modules.account.dto.QryAcctReqDto;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

@SpringBootTest
@Slf4j
public class AcctTest {

    /**
     * 财资管理云公钥(平台公钥)
     */
    static final String bodyEncryptionKey = "04CD09E422766D2412A9BBF802D2185C548412EF9CC20D47357D2975DFEB9CBD5270FA3F17F261B77A471C1800F7391407134C2629FEA3EDD58F67340914F19723";

    /**
     * 企业私钥（加密）
     */
    static final String signEncryptionPrivateKey = "96c0ef256c22c15ff6c5f70669b3a8d6ec1352cb490ecade7c9b3c386cbf22b8";

    /**
     * 企业私钥（解密）
     */
    static final String bodyDecryptionKey = "96c0ef256c22c15ff6c5f70669b3a8d6ec1352cb490ecade7c9b3c386cbf22b8";

    /**
     * 根据appid和appsecert获取的token
     */
    static final String token = "8ed84a09-01e6-481e-a2d8-456a19c9d942";//chongxin

    /**
     * 接口路径
     */
    //static final String TARGET_URL = "http://cbs8-gateway-openapi-dev.paas.cmbchina.cn/openapi/account/accounts-current-balance/erp/query";

    static final String TARGET_URL = "https://tmcapi.cmbchina.com/openapi/account/openapi/v1/account/query";






    /**
     * 请求体数据
     */
    static final String requestData ="{\n" +
            "    \"accountNature\": \"A\",\n" +
            "    \"accountNo\": \"\",\n" +
            "    \"bankTypeList\": [\n" +
            "        \"CMB\",\n" +
            "        \"SDB\"\n" +
            "    ],\n" +
            "    \"currencyList\": [\n" +
            "        \"10\",\n" +
            "        \"11\"\n" +
            "    ],\n" +
            "    \"depositTypeList\": [\n" +
            "        \"A\",\n" +
            "        \"C\"\n" +
            "    ],\n" +
            "    \"directConnectFlag\": \"9\"\n" +
            "}";

    @Test
    public void queryAcct() throws Exception {
        log.info("\n返回结果：{}", new String(requestData));

        QryAcctReqDto qryAcctReqDto = new QryAcctReqDto();
        qryAcctReqDto.setAccountNature("A");
        qryAcctReqDto.setAccountNo("");
        qryAcctReqDto.getBankTypeList().add("CMB");
        qryAcctReqDto.getBankTypeList().add("SDB");
        qryAcctReqDto.getCurrencyList().add("10");
        qryAcctReqDto.getCurrencyList().add("11");
        qryAcctReqDto.getDepositTypeList().add("A");
        qryAcctReqDto.getDepositTypeList().add("C");
        qryAcctReqDto.setDirectConnectFlag("9");
        String requestData = JSONObject.toJSONString(qryAcctReqDto);
        log.info("requestData="+requestData);

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
