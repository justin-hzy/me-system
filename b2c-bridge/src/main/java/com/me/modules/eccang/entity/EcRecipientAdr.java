package com.me.modules.eccang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("EcRecipient")
public class EcRecipientAdr {

    @TableField("firstname")
    private String firstname;

    @TableField("company")
    private String company;

    @TableField("country")
    private String country;

    @TableField("city")
    private String city;

    @TableField("postcode")
    private String postcode;

    @TableField("streetAddress1")
    private String streetAddress1;

    @TableField("streetAddress2")
    private String streetAddress2;

    @TableField("district")
    private String district;

    @TableField("state")
    private String state;

    @TableField("doorplate")
    private String doorplate;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;
}
