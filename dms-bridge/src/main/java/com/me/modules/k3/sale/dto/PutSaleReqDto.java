package com.me.modules.k3.sale.dto;

import com.me.modules.k3.sale.pojo.SaleFEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PutSaleReqDto {

    private String fbillno;

    private String fstockorgid;

    private String fsaleorgid;

    private String fsettleorgid;

    private String fthirdbillno;

    private String fcustomerid;

    private String fsettlecurrid;

    private String fdsgbase;

    private String fdate;

    private String type;

    private String freceiveaddress;

    private List<SaleFEntity> fentitylist;
}
