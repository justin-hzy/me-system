package com.me.modules.lock.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dms_lock_switch")
public class DmsLockSwitch {

    @TableField("switch_type")
    private String switchType;

    @TableField("is_open")
    private String isOpen;
}
