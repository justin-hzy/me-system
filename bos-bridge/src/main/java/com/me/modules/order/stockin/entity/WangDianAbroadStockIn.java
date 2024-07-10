package com.me.modules.order.stockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("WANGDIAN_ABROAD_STOCKIN")
public class WangDianAbroadStockIn {


    @TableField("stockinid")
    private String stockinId;

    @TableField("stockinno")
    private String stockinno;

    @TableField("orderno")
    private String orderNo;

    @TableField("warehouseno")
    private String warehouseNo;

    @TableField("warehousename")
    private String warehouseName;

    @TableField("remark")
    private String remark;

    @TableField("checktime")
    private String checkTime;

    @TableField("stockintime")
    private String stockintime;

    @TableField("shopname")
    private String shopName;

    @TableField("tid")
    private String tid;

}
