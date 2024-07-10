package com.me.modules.eccang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("EcBillingAdr")
public class EcBillingAdr {

    @TableField(value = "firstname")
    private String firstname;

    @TableField(value = "company")
    private String company;

    @TableField(value = "country")
    private String country;

    @TableField(value = "city")
    private String city;

    @TableField(value = "postcode")
    private String postcode;

    @TableField(value = "streetAddress1")
    private String streetAddress1;

    @TableField(value = "streetAddress2")
    private String streetAddress2;

    @TableField(value = "district")
    private String district;

    @TableField(value = "state")
    private String state;

    @TableField(value = "doorplate")
    private String doorplate;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "email")
    private String email;
}
