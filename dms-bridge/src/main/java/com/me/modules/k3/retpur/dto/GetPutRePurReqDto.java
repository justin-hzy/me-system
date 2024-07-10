package com.me.modules.k3.retpur.dto;

import com.me.modules.k3.retpur.pojo.RePurFentity;
import com.me.modules.k3.retsale.pojo.ReSaleFEntity;
import lombok.Data;

import java.util.List;

@Data
public class GetPutRePurReqDto {

    private String fbillno;

    private String fstockorgid;

    private String fpurchaseorgid;

    private String fsupplierid;

    private String fdemandorgid;

    private String fsettlecurrid;

    private String fthirdbillno;

    private String fdate;

    private List<RePurFentity> fentitylist;

}
