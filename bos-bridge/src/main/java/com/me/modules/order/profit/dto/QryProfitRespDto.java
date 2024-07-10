package com.me.modules.order.profit.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QryProfitRespDto {

    private String tradeNo;

    private String tid;

    private String srcOid;

    private String totalIncome;

}
