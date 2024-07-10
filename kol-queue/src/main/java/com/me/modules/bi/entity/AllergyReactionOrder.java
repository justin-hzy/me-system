package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("allergy_reaction_order")
public class AllergyReactionOrder {

    @ApiModelProperty("id")
    @TableField("id")
    private String id;

    @ApiModelProperty("创建人")
    @TableField("creator")
    private String creator;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private String createTime;

    @ApiModelProperty("修改时间")
    @TableField("modify_time")
    private String modifyTime;

    @ApiModelProperty("任务状态")
    @TableField("task_status")
    private String taskStatus;

    @ApiModelProperty("补偿金额")
    @TableField("compensation_amount")
    private String compensationAmount;

    @ApiModelProperty("处理结果")
    @TableField("processing_result")
    private String processingResult;

    @ApiModelProperty("商品信息表")
    @TableField("product_info_table")
    private String productInfoTable;

    @ApiModelProperty("店铺")
    @TableField("shop")
    private String shop;

    @ApiModelProperty("订单号")
    @TableField("order_number")
    private String orderNumber;

    @ApiModelProperty("买家昵称")
    @TableField("buyer_nickname")
    private String buyerNickname;

    @ApiModelProperty("付款时间")
    @TableField("payment_time")
    private String paymentTime;

    @ApiModelProperty("过敏症状")
    @TableField("allergic_symptoms")
    private String allergicSymptoms;

    @ApiModelProperty("异常问题备注")
    @TableField("abnormal_remarks")
    private String abnormalRemarks;

    @ApiModelProperty("过敏图片")
    @TableField("allergic_images")
    private String allergicImages;

    @TableField("is_delete")
    private String isDelete;


}
