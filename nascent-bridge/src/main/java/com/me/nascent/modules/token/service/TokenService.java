package com.me.nascent.modules.token.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.nascent.modules.token.entity.Token;

public interface TokenService extends IService<Token> {

    String getToken() throws Exception;

    String getBtnToken() throws Exception;
}
