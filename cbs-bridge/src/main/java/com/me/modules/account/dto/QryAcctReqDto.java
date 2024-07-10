package com.me.modules.account.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QryAcctReqDto {

    private String accountNature;

    private String accountNo;

    private List<String> bankTypeList = new ArrayList<>();


    private List<String> currencyList = new ArrayList<>();

    private List<String> depositTypeList = new ArrayList<>();

    private String directConnectFlag;
}
