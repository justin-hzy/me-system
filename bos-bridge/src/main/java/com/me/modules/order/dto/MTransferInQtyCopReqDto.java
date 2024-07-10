package com.me.modules.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MTransferInQtyCopReqDto {

    private Integer p_id;

    private Integer p_user_id;

    private BigDecimal r_code;

    private String r_message;
}
