package com.me.modules.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MTransferInSubmitReqDto {

    private Integer p_submittedsheetid;

    private BigDecimal r_code;

    private String r_message;
}
