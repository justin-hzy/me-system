package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.IssueType;
import com.me.modules.bi.mapper.IssueTypeMapper;
import com.me.modules.bi.service.IssueTypeService;
import org.springframework.stereotype.Service;

@Service
public class IssueTypeServiceImpl extends ServiceImpl<IssueTypeMapper, IssueType> implements IssueTypeService {
}
