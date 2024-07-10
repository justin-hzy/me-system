package com.me.modules.sys.request;

import org.apache.http.client.methods.HttpPost;

public interface RequestService {

    HttpPost setupRequest(String requestData, String token,String signEncryptionPrivateKey,String TARGET_URL,String bodyEncryptionKey);
}
