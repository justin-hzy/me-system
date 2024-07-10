package com.me.modules.k3.purchase.dto;

import com.me.modules.k3.purchase.pojo.PurFEntry;
import com.me.modules.k3.sale.pojo.SaleFEntity;
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
