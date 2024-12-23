package com.me.modules.mabang.refund.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InsertMBRefundDto {

    private String platformOrderId;


    private String currencyId;


    private String refundTime;


    private String shopId;


    private String status;

    List<InsertMBRefundDtl> insertMBRefundDtls = new ArrayList<>();
}
