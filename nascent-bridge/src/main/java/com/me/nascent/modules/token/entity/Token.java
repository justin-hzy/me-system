package com.me.nascent.modules.token.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("secret")
@Data
public class Token {

    private String name;

    private String token;
}
