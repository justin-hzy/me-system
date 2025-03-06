package com.me.modules.nascent.point.service;

import java.util.Map;

public interface TransPointLogService {

    Map<String,Object> transPointLog(String startDateStr, String endDateStr, Long nextId) throws Exception;
}
