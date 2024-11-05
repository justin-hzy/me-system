package com.me.modules.resale.dto;

import com.me.modules.resale.pojo.ReSaleFEntity;
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
