package com.me.modules.bi.service;

import java.util.Map;

public interface ByteNewEumService {

    void initReissueOrder();

    void initStoreAfSalesException();

    void initAllergyReactionOrder();

    Map<String,Integer> initUser();

    void initUser(String pageNum);

    void initShop();
}
