package com.me.modules.sys.response.impl;

import com.me.common.enums.Constants;
import com.me.common.utils.SM2Util;
import com.me.modules.sys.response.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
public class ResponseServiceImpl implements ResponseService {

    @Override
    public byte[] handleResponse(HttpResponse response,String bodyDecryptionKey) throws IOException {
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
