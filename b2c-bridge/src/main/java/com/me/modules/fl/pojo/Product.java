package com.me.modules.fl.pojo;

import lombok.Data;

@Data
public class Product {

    private String sku;

    private String identifier;

    private String product_storage_type;

    private String quantity;

    private String shipped_quantity;

}
