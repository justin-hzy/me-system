package com.me.nascent.modules.token.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.nascent.modules.token.entity.Token;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper extends BaseMapper<Token> {
}
