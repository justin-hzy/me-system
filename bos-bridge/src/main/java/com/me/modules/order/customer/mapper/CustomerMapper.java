package com.me.modules.order.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.customer.dto.QueryIdAndCodeRespDto;
import com.me.modules.order.customer.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    QueryIdAndCodeRespDto queryIdAndCode(@Param("remark") String remark);
}
