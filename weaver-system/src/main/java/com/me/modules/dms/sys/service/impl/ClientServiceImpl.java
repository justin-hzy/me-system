package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.dms.sys.entity.Client;
import com.me.modules.dms.sys.mapper.ClientMapper;
import com.me.modules.dms.sys.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {
}
