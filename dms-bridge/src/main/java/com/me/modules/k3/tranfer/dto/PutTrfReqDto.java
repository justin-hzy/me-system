package com.me.modules.k3.tranfer.dto;

import com.me.modules.k3.sale.pojo.SaleFEntity;
import com.me.modules.k3.tranfer.pojo.TrfFEntity;
import lombok.Data;

import java.util.List;

@Data
public class PutTrfReqDto {

    private String fstockoutorgid;

    private String fdate;

    private String fbillNo;

    private String fthirdsrcbillno;

    private List<TrfFEntity> fentitylist;
}
