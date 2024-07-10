package com.me.modules.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.common.core.JsonResult;
import com.me.modules.demo.dto.DemoReqDto;
import com.me.modules.demo.entity.BPayable;
import com.me.modules.demo.mapper.BPayableMapper;
import com.me.modules.demo.service.BPayableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class BPayableServiceImpl extends ServiceImpl<BPayableMapper, BPayable> implements BPayableService {

    private final BPayableMapper bPayableMapper;

    @Override
    public JsonResult<BPayable> queryBPayable(DemoReqDto demoReqDto) {
        log.info("执行queryBPayable,id="+demoReqDto.getId());
        BPayable bPayable = bPayableMapper.queryBPayable(demoReqDto.getId());
        log.info("bPayable="+bPayable.toString());
        return JsonResult.ok();
    }
}
