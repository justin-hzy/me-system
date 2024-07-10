package com.me.modules.rate.dto;

import lombok.Data;

@Data
public class QryRateReqDto {

    private String validDateStart;

    private String validDateEnd;

    private Integer pageSize;
}
