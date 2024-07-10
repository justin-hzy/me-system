package com.me.modules.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("WANGDIAN_APIGOOGS_DEMO2")
public class WangDianApiGoods {

    @TableId(type = IdType.NONE)
    private BigDecimal id;

    @TableField(value = "PLATFORMID")
    private String platFormId;

    @TableField(value = "SHOPNO")
    private String shopNo;

    @TableField(value = "EDIFLAG")
    private BigDecimal EdiFlag;

    @TableField(value = "RESULTS")
    private String results;

    @TableField(value = "CREATETIME")
    private Date createTime;

    @TableField(value = "UPDATETIME")
    private Date updateTime;

    @TableField(value = "M_PRODUCT_ID")
    private BigDecimal MProductId;
}
