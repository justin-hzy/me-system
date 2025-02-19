package com.me.modules.queue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.queue.entity.DmsTwQueue;
import com.me.modules.queue.mapper.DmsTwQueueMapper;
import com.me.modules.queue.service.DmsTwQueueService;
import org.springframework.stereotype.Service;

@Service
public class DmsTwQueueServiceImpl extends ServiceImpl<DmsTwQueueMapper, DmsTwQueue> implements DmsTwQueueService {
}
