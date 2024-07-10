package com.me.modules.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.common.core.JsonResult;
import com.me.modules.demo.dto.DemoReqDto;
import com.me.modules.demo.entity.BPayable;

public interface BPayableService extends IService<BPayable> {

    JsonResult<BPayable> queryBPayable(DemoReqDto demoReqDto);
}
