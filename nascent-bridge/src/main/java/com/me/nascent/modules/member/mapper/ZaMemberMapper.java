package com.me.nascent.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.nascent.modules.member.entity.PureMember;
import com.me.nascent.modules.member.entity.ZaMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ZaMemberMapper extends BaseMapper<ZaMember> {
}
