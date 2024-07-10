package com.me.modules.bi.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.AllergicSymptoms;
import com.me.modules.bi.mapper.AllergicSymptomsMapper;
import com.me.modules.bi.service.AllergicSymptomsService;
import org.springframework.stereotype.Service;

@Service
public class AllergicSymptomsServiceImpl extends ServiceImpl<AllergicSymptomsMapper,AllergicSymptoms> implements AllergicSymptomsService {

}
