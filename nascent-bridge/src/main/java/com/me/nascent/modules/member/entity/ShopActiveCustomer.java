package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("shop_active_customer")
public class ShopActiveCustomer {
    @TableField("id")
    private Long id;

    @TableField("updateTime")
    private Date updateTime;

    @TableField("activateTime")
    private Date activateTime;

    @TableField("bindTime")
    private Date bindTime;

    @TableField("shopId")
    private Long shopId;

    @TableField("customerName")
    private String customerName;

    @TableField("customerHeadImage")
    private String customerHeadImage;

    @TableField("customerFrom")
    private Integer customerFrom;

    @TableField("customerRemark")
    private String customerRemark;

    @TableField("sex")
    private Integer sex;

    @TableField("qq")
    private String qq;

    @TableField("outAlias")
    private String outAlias;

    @TableField("birthday")
    private Date birthday;

    @TableField("mobile")
    private String mobile;

    @TableField("email")
    private String email;

    @TableField("buyerAlipayNo")
    private String buyerAlipayNo;

    @TableField("marryStatus")
    private Integer marryStatus;

    @TableField("subPlatform")
    private Integer subPlatform;

    @TableField("isActivate")
    private Integer isActivate;

    @TableField("babyName")
    private String babyName;

    @TableField("babySex")
    private Integer babySex;

    @TableField("babyBirthday")
    private Date babyBirthday;

    @TableField("country")
    private String country;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("district")
    private String district;

    @TableField("address")
    private String address;
}
