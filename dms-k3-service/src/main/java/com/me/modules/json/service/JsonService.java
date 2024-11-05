package com.me.modules.json.service;

import com.me.modules.assembly.dto.PutAssemblyDto;
import com.me.modules.purchase.dto.PutPurReqDto;
import com.me.modules.retpur.dto.GetPutRePurReqDto;
import com.me.modules.resale.dto.PutReSaleReqDto;
import com.me.modules.sale.dto.PutSaleReqDto;
import com.me.modules.tranfer.dto.PutTrfReqDto;

public interface JsonService {

    String getSaveSaleJsons(PutSaleReqDto dto);

    String getSaveReSalJsons(PutReSaleReqDto dto);

    String getSavePurchaseJsons(PutPurReqDto dto);

    String getSaveRetPurJsons(GetPutRePurReqDto dto);

    String getSaveTrfJson(PutTrfReqDto dto);

    String getSaveAssyJson(PutAssemblyDto dto);
}
