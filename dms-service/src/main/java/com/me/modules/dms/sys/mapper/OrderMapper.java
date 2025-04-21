package com.me.modules.dms.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.dms.sys.entity.Order;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
