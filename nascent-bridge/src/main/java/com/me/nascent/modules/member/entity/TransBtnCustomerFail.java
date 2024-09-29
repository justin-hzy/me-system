package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trans_btn_customer_fail")
public class TransBtnCustomerFail {

    @TableField("id")
    private String id;

    @TableField("message")
    private String message;

    @TableField("type")
    private String type;
}
