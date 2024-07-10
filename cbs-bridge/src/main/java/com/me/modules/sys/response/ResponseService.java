package com.me.modules.sys.response;

import org.apache.http.HttpResponse;

import java.io.IOException;

public interface ResponseService {

    byte[] handleResponse(HttpResponse response,String bodyDecryptionKey) throws IOException;
}
