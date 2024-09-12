package com.me.modules.purchase.dto;

import com.me.modules.purchase.pojo.PurFEntry;
import com.me.modules.sale.pojo.SaleFEntity;
import lombok.Data;

import java.util.List;

@Data
public class PutPurReqDto {

    private String fbillno;

    private String fstockorgid;

    private String fpurchaseorgid;

    private String fsupplierId;

    private String fthirdbillno;

    private String fdemandorgid;

    private String fdate;

    private String fsettlecurrid;

    private String fisincludedtax;

    private String falldiscount;

    private List<PurFEntry> fentrylist;
}
