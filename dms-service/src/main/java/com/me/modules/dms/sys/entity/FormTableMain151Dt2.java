package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_151_dt2")
public class FormTableMain151Dt2 {

    @TableField("id")
    private String id;

    @TableField("mainid")
    private String mainid;

    @TableField("hpbh")
    private String hpbh;

    @TableField("cksl")
    private String cksl;

    @TableField("xsckdh")
    private String xsckdh;

    @TableField("chrq")
    private String chrq;

    @TableField("shsl")
    private String shsl;
}
