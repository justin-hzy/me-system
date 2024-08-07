package com.me.nascent.modules.reorder.service;

import java.util.Date;

public interface TransReOrderService {

    void transReOrder(Long nextId, Date startDate, Date endDate) throws Exception;

    void transReOrder(Date startDate, Date endDate) throws Exception;

    void putReOrder() throws Exception;

}
