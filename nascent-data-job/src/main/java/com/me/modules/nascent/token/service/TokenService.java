package com.me.modules.nascent.token.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.nascent.token.entity.Token;

public interface TokenService extends IService<Token> {


    String getBtnToken() throws Exception;
}
