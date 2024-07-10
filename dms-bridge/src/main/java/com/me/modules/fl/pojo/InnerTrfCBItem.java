package com.me.modules.fl.pojo;

import lombok.Data;

@Data
public class InnerTrfCBItem {

    private String sku;

    private String original_expiration_date;

    private String original_batch;

    private String original_storage_type;

    private String expiration_date;

    private String batch;

    private String storage_type;

    private String quantity;

    private String transferred_quantity;

}
