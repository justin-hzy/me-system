package com.me.nascent.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.me.nascent.modules.order.entity.Nick;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NickMapper extends BaseMapper<Nick> {
}
