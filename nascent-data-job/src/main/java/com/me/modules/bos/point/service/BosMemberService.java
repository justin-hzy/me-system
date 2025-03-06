package com.me.modules.bos.point.service;

import com.me.common.core.JsonResult;

public interface BosMemberService {

    JsonResult getBosMemberCount();

    JsonResult getBosOffMembers(Integer count);

    JsonResult getBosOffPoint(Integer count);
}
