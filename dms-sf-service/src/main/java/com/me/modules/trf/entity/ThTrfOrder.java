package com.me.modules.trf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_356")
public class ThTrfOrder {

    @TableField("erp_order")
    private String erpOrder;

    @TableField("requestid")
    private String requestId;

    @TableField("is_send")
    private String isSend;

    @TableField("is_receive")
    private String isReceive;

    @TableField("out_company_code")
    private String outCompanyCode;

    @TableField("in_company_code")
    private String inCompanyCode;
}
