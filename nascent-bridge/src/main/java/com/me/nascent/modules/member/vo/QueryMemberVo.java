package com.me.nascent.modules.member.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class QueryMemberVo {


    private Long id;


    private Date updateTime;


    private Date activateTime;


    private Date bindTime;


    private Long shopId;


    private String customerName;


    private String customerHeadImage;


    private Integer customerFrom;


    private String customerRemark;


    private Integer sex;


    private String qq;


    private String outAlias;


    private Date birthday;


    private String mobile;


    private String email;


    private String buyerAlipayNo;


    private Integer marryStatus;


    private Integer subPlatform;


    private Integer isActivate;


    private String babyName;


    private Integer babySex;


    private Date babyBirthday;


    private String country;


    private String province;


    private String city;


    private String district;


    private String address;


    private String nick;


    private String nasOuid;


    private Integer platform;
}
