package com.me.nascent.modules.order.service;

import java.util.Date;
import java.util.Map;

public interface TransOrderService {

    Map<String,Object> transOrder(Long nextId, Date startDateStr, Date setEndTimeStr) throws Exception;
}
