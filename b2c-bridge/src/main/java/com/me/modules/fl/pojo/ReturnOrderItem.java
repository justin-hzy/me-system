package com.me.modules.fl.pojo;

import lombok.Data;

@Data
public class ReturnOrderItem {

    private String sku;

    private String storage_type;

    private String expiration_date;

    private String batch;

    private String quantity;

    private String received_pcs;

    private String identifier;

}
