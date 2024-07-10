package com.me.modules.order.msale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.msale.entity.MSale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MSaleMapper extends BaseMapper<MSale> {

    Integer queryCount(@Param("tid") String tid);
}
