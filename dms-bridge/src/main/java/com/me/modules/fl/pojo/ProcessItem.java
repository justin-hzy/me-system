package com.me.modules.fl.pojo;

import lombok.Data;

@Data
public class ProcessItem {

    private String sku;

    private String storage_type;

    private String expiration_date;

    private String batch;

    private Integer received_pcs;
}
