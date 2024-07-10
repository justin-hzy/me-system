package com.me.modules.order.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.store.entity.CStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CStoreMapper extends BaseMapper<CStore> {

    CStore queryIds(@Param("wareHouseNo")String wareHouseNo);

    CStore queryCstoreByShopName(@Param("shopName")String shopName);

    String queryName(@Param("wareHouseNo")String wareHouseNo);
}
