package com.me.modules.tran.service;

import com.me.modules.assembly.dto.PutAssemblyDto;
import com.me.modules.purchase.dto.PutPurReqDto;
import com.me.modules.retpur.dto.GetPutRePurReqDto;
import com.me.modules.resale.dto.PutReSaleReqDto;
import com.me.modules.sale.dto.PutSaleReqDto;
import com.me.modules.tranfer.dto.PutTrfReqDto;

public interface TranService {

    String tranHkSaleOrder(PutSaleReqDto dto) throws Exception;

    String tranTWSaleOrder(PutSaleReqDto dto) throws Exception;

    String putHkReSaleOrder(PutReSaleReqDto dto) throws Exception;

    String putTwReSaleOrder(PutReSaleReqDto dto) throws Exception;

    String tranHkPurchase(PutPurReqDto dto) throws Exception;

    String tranTwPurchase(PutPurReqDto dto) throws Exception;

    String tranHkRePur(GetPutRePurReqDto dto) throws Exception;

    String tranTwRePur(GetPutRePurReqDto dto) throws Exception;


    String putHkTrfUrl(PutTrfReqDto dto) throws Exception;

    String putTwTrfUrl(PutTrfReqDto dto) throws Exception;

    String tranAssembly(PutAssemblyDto dto) throws Exception;
}
