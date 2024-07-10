package com.me.modules.fl.service;

import java.util.List;

public interface FlReOrderService {

    void sendOrder(String param);

    List<String> queryReOrder();
}
