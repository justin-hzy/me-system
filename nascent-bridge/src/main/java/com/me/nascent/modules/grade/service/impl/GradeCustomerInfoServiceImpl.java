package com.me.nascent.modules.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.grade.entity.GradeCustomerInfo;
import com.me.nascent.modules.grade.mapper.GradeCustomerInfoMapper;
import com.me.nascent.modules.grade.service.GradeCustomerInfoService;
import org.springframework.stereotype.Service;

@Service
public class GradeCustomerInfoServiceImpl extends ServiceImpl<GradeCustomerInfoMapper, GradeCustomerInfo> implements GradeCustomerInfoService {
}
