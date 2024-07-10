package com.me.modules.k3.tran.service;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.k3.assembly.dto.PutAssemblyDto;
import com.me.modules.k3.purchase.dto.PutPurReqDto;
import com.me.modules.k3.retpur.dto.GetPutRePurReqDto;
import com.me.modules.k3.retsale.dto.PutReSaleReqDto;
import com.me.modules.k3.sale.dto.PutSaleReqDto;
import com.me.modules.k3.tranfer.dto.PutTrfReqDto;

public interface TranService {

    String tranSaleOrder(PutSaleReqDto dto) throws Exception;

    void tranSaleReOrder(PutReSaleReqDto dto) throws Exception;

    String tranPurchase(PutPurReqDto dto) throws Exception;

    String tranRetPur(GetPutRePurReqDto dto) throws Exception;

    String tranTrf(PutTrfReqDto dto) throws Exception;

    String tranAssembly(PutAssemblyDto dto) throws Exception;
}
