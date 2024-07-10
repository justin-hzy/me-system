package com.me.modules.k3.retsale.dto;

import com.me.modules.k3.retsale.pojo.ReSaleFEntity;
import com.me.modules.k3.sale.pojo.SaleFEntity;
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
