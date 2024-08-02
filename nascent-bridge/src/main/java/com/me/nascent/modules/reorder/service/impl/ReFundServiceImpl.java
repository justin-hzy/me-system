package com.me.nascent.modules.reorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.reorder.mapper.ReFundMapper;
import com.me.nascent.modules.reorder.service.ReFundService;
import org.springframework.stereotype.Service;

@Service
public class ReFundServiceImpl extends ServiceImpl<ReFundMapper,ReFund> implements ReFundService {
}
