package com.me.modules.lock.controller;

import com.me.common.core.JsonResult;
import com.me.modules.lock.dto.UnTwLockReqDto;
import com.me.modules.lock.service.LockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("twLock")
@RestController
@AllArgsConstructor
public class TwLockController {

    private LockService lockService;

    @PostMapping("unTwLock")
    public JsonResult unTwLock(@RequestBody UnTwLockReqDto dto){
        JsonResult respJsonResult = lockService.unLock(dto);
        return respJsonResult;
    }
}
