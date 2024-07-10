package com.me.modules.bi.service;

import java.util.Map;

public interface TransByteNewDataService {

    Map<String,Integer> transStoreAfSalesException();

    void transStoreAfSalesException(String pageNum);

    Map<String, Integer> transRecycleStoreAfSalesException();

    void transRecycleStoreAfSalesException(String pageNum);

    Map<String,Integer> transAllergyReactionOrder();

    void transAllergyReactionOrder(String pageNum);

    Map<String, Integer> transRecycleAllergyReactionOrder();

    void transRecycleAllergyReactionOrder(String pageNum);

    Map<String,Integer> transReissueOrder();

    void transReissueOrder(String pageNum);

    Map<String,Integer> transRecycleTransReissueOrder();

    void transRecycleTransReissueOrder(String pageNum);



}
