package com.me.nascent.modules.trans.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trans_btn_trade_fail")
public class TransBtnTradeFail {

    @TableId("id")
    private String id;

    @TableField("message")
    private String message;
}
