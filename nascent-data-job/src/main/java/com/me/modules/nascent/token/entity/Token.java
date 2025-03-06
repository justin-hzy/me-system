package com.me.modules.nascent.token.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("secret")
@Data
public class Token {

    private String name;

    private String token;
}
