package com.me.modules.mabang.service;

import java.util.Map;

public interface MaBangHttpService {

    String callGwApi(String api, Map<String, Object> reqParams);
}
