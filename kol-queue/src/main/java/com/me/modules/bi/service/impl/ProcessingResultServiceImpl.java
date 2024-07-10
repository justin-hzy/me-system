package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.ProcessingResult;
import com.me.modules.bi.mapper.ProcessingResultMapper;
import com.me.modules.bi.service.ProcessingResultService;
import org.springframework.stereotype.Service;

@Service
public class ProcessingResultServiceImpl extends ServiceImpl<ProcessingResultMapper, ProcessingResult> implements ProcessingResultService {
}
