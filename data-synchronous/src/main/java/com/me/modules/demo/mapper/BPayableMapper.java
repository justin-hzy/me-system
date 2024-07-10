package com.me.modules.demo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.demo.dto.DemoReqDto;
import com.me.modules.demo.entity.BPayable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BPayableMapper extends BaseMapper<BPayable> {
    BPayable queryBPayable(@Param("id") String id);
}
