package com.me.modules.nascent.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("off_store_info")
public class OffStoreInfo {

    @TableField("name")
    private String name;

    @TableField("provider_guid")
    private String providerGuid;
}
