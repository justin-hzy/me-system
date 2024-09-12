package com.me.modules.tranfer.dto;

import com.me.modules.sale.pojo.SaleFEntity;
import com.me.modules.tranfer.pojo.TrfFEntity;
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
