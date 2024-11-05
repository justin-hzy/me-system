package com.me.modules.retpur.dto;

import com.me.modules.retpur.pojo.RePurFentity;
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
