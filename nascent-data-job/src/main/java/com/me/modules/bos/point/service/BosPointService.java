package com.me.modules.bos.point.service;

import com.me.common.core.JsonResult;
import com.me.modules.nascent.point.entity.PointLog;

public interface BosPointService {

    JsonResult putBosPoint(PointLog pointLog);

    JsonResult submitBosPoint(Integer objectId);
}
