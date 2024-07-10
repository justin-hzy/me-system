package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReissueOrder {

    @ApiModelProperty("id")
    @TableField("id")
    private String id;

    @ApiModelProperty("任务状态")
    @TableField("task_status")
    private String taskStatus;

    @ApiModelProperty("创建人")
    @TableField("creator")
    private String creator;

    @ApiModelProperty("店铺【旺店通】")
    @TableField("shop")
    private String shop;

    @ApiModelProperty("订单号")
    @TableField("original_order_number")
    private String originalOrderNumber;

    @ApiModelProperty("收寄信息")
    @TableField("receiving_information")
    private String receivingInformation;

    @ApiModelProperty("收寄信息")
    @TableField("reissue_reason")
    private String reissueReason;

    @ApiModelProperty("订单备注")
    @TableField("order_note")
    private String orderNote;

    @ApiModelProperty("客服备注")
    @TableField("customer_service_note")
    private String customerServiceNote;

    @ApiModelProperty("商品信息")
    @TableField("product_info")
    private String productInfo;

    @ApiModelProperty("网名")
    @TableField("net_name")
    private String netName;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private String createTime;

    @ApiModelProperty("修改时间")
    @TableField("modify_time")
    private String modifyTime;

    @ApiModelProperty("任务完结时间")
    @TableField("task_finish_time")
    private String taskFinishTime;

    @ApiModelProperty("任务完结人")
    @TableField("task_finisher")
    private String taskFinisher;

    @ApiModelProperty("逻辑删除标志")
    @TableField("is_delete")
    private String isDelete;
}
