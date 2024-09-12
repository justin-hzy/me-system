package com.me.modules.retsale.dto;

import com.me.modules.retsale.pojo.ReSaleFEntity;
import com.me.modules.sale.pojo.SaleFEntity;
import lombok.Data;

import java.util.List;

@Data
public class PutReSaleReqDto {

    private String fbillno;

    private String fstockorgid;

    private String fsaleorgid;

    private String fsettleorgid;

    private String fthirdbillno;

    private String fretcustid;

    private String fsettlecurrid;

    private String fdate;

    private List<ReSaleFEntity> fentitylist;
}
