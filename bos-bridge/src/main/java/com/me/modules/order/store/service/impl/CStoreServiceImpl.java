package com.me.modules.order.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.store.entity.CStore;
import com.me.modules.order.store.mapper.CStoreMapper;
import com.me.modules.order.store.service.CStoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CStoreServiceImpl extends ServiceImpl<CStoreMapper, CStore> implements CStoreService {

    private CStoreMapper cStoreMapper;


    @Override
    public CStore queryIds(String wareHouseNo) {
        return cStoreMapper.queryIds(wareHouseNo);
    }

    @Override
    public CStore queryCstoreByShopName(String shopName) {
        return cStoreMapper.queryCstoreByShopName(shopName);
    }

    @Override
    public String queryName(String wareHouseNo) {
        return cStoreMapper.queryName(wareHouseNo);
    }
}
