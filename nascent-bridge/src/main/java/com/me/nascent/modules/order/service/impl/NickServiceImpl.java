package com.me.nascent.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.order.entity.Nick;
import com.me.nascent.modules.order.mapper.NickMapper;
import com.me.nascent.modules.order.service.NickService;
import org.springframework.stereotype.Service;

@Service
public class NickServiceImpl extends ServiceImpl<NickMapper, Nick> implements NickService {
}
