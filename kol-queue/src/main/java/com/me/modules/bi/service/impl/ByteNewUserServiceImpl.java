package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.ByteNewUser;
import com.me.modules.bi.mapper.ByteNewUserMapper;
import com.me.modules.bi.service.ByteNewUserService;
import org.springframework.stereotype.Service;

@Service
public class ByteNewUserServiceImpl extends ServiceImpl<ByteNewUserMapper, ByteNewUser> implements ByteNewUserService {
}
