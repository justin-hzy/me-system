package com.me.modules.bos.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.bos.member.dto.QueryBosMemberDto;
import com.me.modules.bos.member.entity.BosMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BosMemberMapper extends BaseMapper<BosMember> {

    List<QueryBosMemberDto> queryBosMember();
}
