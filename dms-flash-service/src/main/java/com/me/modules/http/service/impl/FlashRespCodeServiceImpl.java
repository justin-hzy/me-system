package com.me.modules.http.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.http.entity.FlashRespCode;
import com.me.modules.http.mapper.FlashRespCodeMapper;
import com.me.modules.http.service.FlashRespCodeService;
import org.springframework.stereotype.Service;

@Service
public class FlashRespCodeServiceImpl extends ServiceImpl<FlashRespCodeMapper, FlashRespCode> implements FlashRespCodeService {
}
