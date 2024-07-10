package com.me.modules.transaction.dto;

import lombok.Data;


@Data
public class QryTxnReqDto {

    private String dateType;

    private String startDate;

    private String endDate;

    private int pageSize;

    private int currentPage;
}
