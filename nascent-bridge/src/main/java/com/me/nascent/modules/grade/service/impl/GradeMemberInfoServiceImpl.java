package com.me.nascent.modules.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.grade.entity.GradeMemberInfo;
import com.me.nascent.modules.grade.mapper.GradeMemberInfoMapper;
import com.me.nascent.modules.grade.service.GradeMemberInfoService;
import org.springframework.stereotype.Service;

@Service
public class GradeMemberInfoServiceImpl extends ServiceImpl<GradeMemberInfoMapper, GradeMemberInfo> implements GradeMemberInfoService {
}
