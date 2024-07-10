package com.me.modules.order.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.customer.dto.QueryIdAndCodeRespDto;
import com.me.modules.order.customer.entity.Customer;
import org.apache.ibatis.annotations.Param;

public interface CustomerService extends IService<Customer>{

    QueryIdAndCodeRespDto queryIdAndCode(@Param("remark") String remark);
}
