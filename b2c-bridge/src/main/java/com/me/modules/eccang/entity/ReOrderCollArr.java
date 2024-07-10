package com.me.modules.eccang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class ReOrderCollArr {
    @TableField("buyer_id")
    private String buyerId;

    @TableField("refrence_no_platform")
    private String refrenceNoPlatform;

    @TableField("Name")
    private String name;

    @TableField("Phone")
    private String phone;

    @TableField("Company_name")
    private String companyName;

    @TableField("Country")
    private String country;

    @TableField("StateOrProvince")
    private String stateOrProvince;

    @TableField("Street1")
    private String street1;

    @TableField("Street2")
    private String street2;

    @TableField("CityName")
    private String cityName;

    @TableField("District")
    private String district;

    @TableField("PostalCode")
    private String postalCode;

    @TableField("doorplate")
    private String doorplate;
}
