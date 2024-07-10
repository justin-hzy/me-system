package com.me.modules.order.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.customer.dto.QueryIdAndCodeRespDto;
import com.me.modules.order.customer.entity.Customer;
import com.me.modules.order.customer.mapper.CustomerMapper;
import com.me.modules.order.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    private CustomerMapper customerMapper;


    @Override
    public QueryIdAndCodeRespDto queryIdAndCode(String remark) {
        return customerMapper.queryIdAndCode(remark);
    }
}
