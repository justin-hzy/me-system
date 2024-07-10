package com.me.modules.k3.json.service;

import com.me.modules.k3.assembly.dto.PutAssemblyDto;
import com.me.modules.k3.purchase.dto.PutPurReqDto;
import com.me.modules.k3.retpur.dto.GetPutRePurReqDto;
import com.me.modules.k3.retsale.dto.PutReSaleReqDto;
import com.me.modules.k3.sale.dto.PutSaleReqDto;
import com.me.modules.k3.tranfer.dto.PutTrfReqDto;


import java.util.List;
import java.util.Map;

public interface JsonService {

    String getSaveSaleJsons(PutSaleReqDto dto);

    String getSaveReSalJsons(PutReSaleReqDto dto);

    String getSavePurchaseJsons(PutPurReqDto dto);

    String getSaveRetPurJsons(GetPutRePurReqDto dto);

    String getSaveTrfJson(PutTrfReqDto dto);

    String getSaveAssyJson(PutAssemblyDto dto);
}
