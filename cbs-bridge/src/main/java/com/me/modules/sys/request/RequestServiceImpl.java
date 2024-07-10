package com.me.modules.sys.request;

import com.me.common.enums.Constants;
import com.me.common.utils.SM2Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    @Override
    public HttpPost setupRequest(String requestData, String token,String signEncryptionPrivateKey,String TARGET_URL,String bodyEncryptionKey) {
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
}
