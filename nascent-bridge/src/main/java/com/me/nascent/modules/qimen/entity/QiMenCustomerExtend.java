package com.me.nascent.modules.qimen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class QiMenCustomerExtend {

    @TableField("babyBirthday")
    private String babyBirthday;

    @TableField("birthday")
    private String birthday;

    @TableField("city")
    private String city;

    @TableField("province")
    private String province;

    @TableField("email")
    private String email;

    @TableField("sex")
    private String sex;

    @TableField("name")
    private String name;

}
