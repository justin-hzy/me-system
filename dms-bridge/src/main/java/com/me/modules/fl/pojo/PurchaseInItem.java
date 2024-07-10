package com.me.modules.fl.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class PurchaseInItem {

    private String sku;

    private String storage_type;

    private String expiration_date;

    private String batch;

    private String received_pcs;
}
