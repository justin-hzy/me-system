package com.me.modules.order.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.store.entity.CStore;

public interface CStoreService extends IService<CStore> {

    CStore queryIds(String wareHouseNo);

    CStore queryCstoreByShopName(String shopName);

    String queryName(String wareHouseNo);
}
