package com.me.modules.lock.service;

import com.me.common.core.JsonResult;
import com.me.modules.lock.dto.UnTwLockReqDto;

public interface LockService {

    JsonResult unLock(UnTwLockReqDto dto);
}
