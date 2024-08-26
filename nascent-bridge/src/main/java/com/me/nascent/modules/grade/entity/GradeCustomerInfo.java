package com.me.nascent.modules.grade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("grade_customer_info")
public class GradeCustomerInfo {

    @TableField("mainId")
    private long mainId;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private Integer platform;

    /*@TableField("activateTime")
    private Date activateTime;

    @TableField("address")
    private String address;

    @TableField("babyBirthday")
    private Date babyBirthday;

    @TableField("babyName")
    private String babyName;

    @TableField("babySex")
    private Integer babySex;

    @TableField("birthday")
    private Date birthday;

    @TableField("buyerAlipayNo")
    private String buyerAlipayNo;

    @TableField("cardReceiveTime")
    private Date cardReceiveTime;

    @TableField("city")
    private String city;

    @TableField("customerFrom")
    private Integer customerFrom;

    @TableField("customerHeadImage")
    private String customerHeadImage;

    @TableField("customerName")
    private String customerName;

    @TableField("customerRemark")
    private String customerRemark;

    @TableField("discount")
    private BigDecimal discount;

    @TableField("district")
    private String district;

    @TableField("email")
    private String email;

    @TableField("extJson")
    private String extJson;*/

    @TableField("grade")
    private Integer grade;

    @TableField("gradeName")
    private String gradeName;

    /*@TableField("isActivate")
    private Integer isActivate;

    @TableField("isMemberBlack")
    private Integer isMemberBlack;

    @TableField("isTBMemberShip")
    private Integer isTBMemberShip;

    @TableField("isUnsubscribe")
    private Integer isUnsubscribe;

    @TableField("lastConcernTime")
    private Date lastConcernTime;

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

    @TableField("rightBlackStr")
    private String rightBlackStr;

    @TableField("sex")
    private Integer sex;

    @TableField("shopId")
    private Long shopId;

    @TableField("subPlatform")
    private Integer subPlatform;

    @TableField("svMobile")
    private String svMobile;

    @TableField("telPhone")
    private String telPhone;*/

    @TableField("updateTime")
    private Date updateTime;

    /*@TableField("userType")
    private Integer userType;*/
}
