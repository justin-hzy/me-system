package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_228")
public class FlConsOrderFormTableMain {

    @TableField("requestid")
    private int requestid;
}
