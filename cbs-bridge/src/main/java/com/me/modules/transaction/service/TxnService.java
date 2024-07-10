package com.me.modules.transaction.service;

import java.io.IOException;
import java.util.Map;

public interface TxnService {

    Map<String,Integer> queryTxn(int currentPage,String date) throws IOException;




}
