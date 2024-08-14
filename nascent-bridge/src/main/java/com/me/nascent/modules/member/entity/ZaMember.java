package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("za_member")
public class ZaMember {

    @TableField("id")
    private Long id;

    @TableField("activateTime")
    private Date activateTime;

    @TableField("address")
    private String address;

    @TableField("babyBirthday")
    private String babyBirthday;

    @TableField("babyName")
    private String babyName;

    @TableField("babySex")
    private Integer babySex;

    @TableField("bindTime")
    private Date bindTime;

    @TableField("birthday")
    private String birthday;

    @TableField("buyerAlipayNo")
    private String buyerAlipayNo;

    @TableField("city")
    private String city;

    @TableField("country")
    private String country;

    @TableField("customerFrom")
    private Integer customerFrom;

    @TableField("customerHeadImage")
    private String customerHeadImage;

    @TableField("customerName")
    private String customerName;

    @TableField("customerRemark")
    private String customerRemark;

    @TableField("discount")
    private String discount;

    @TableField("district")
    private String district;

    @TableField("email")
    private String email;

    @TableField("grade")
    private Integer grade;

    @TableField("gradeName")
    private String gradeName;

    @TableField("isActivate")
    private Integer isActivate;

    @TableField("marryStatus")
    private Integer marryStatus;

    @TableField("memberCard")
    private String memberCard;

    @TableField("mobile")
    private String mobile;

    @TableField("outAlias")
    private String outAlias;

    @TableField("province")
    private String province;

    @TableField("qq")
    private String qq;

    @TableField("sex")
    private Integer sex;

    @TableField("sgGuideId")
    private Integer sgGuideId;

    @TableField("sgRecruitGuideId")
    private Integer sgRecruitGuideId;

    @TableField("sgRecruitShopId")
    private Long sgRecruitShopId;

    @TableField("sgShopId")
    private Long sgShopId;

    @TableField("source")
    private String source;

    @TableField("subPlatform")
    private Integer subPlatform;

    @TableField("updateTime")
    private Date updateTime;
    
}
