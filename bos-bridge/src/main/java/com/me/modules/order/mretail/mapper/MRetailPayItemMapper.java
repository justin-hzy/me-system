package com.me.modules.order.mretail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.dto.QryMRetailPayItemRespDto;
import com.me.modules.order.mretail.entity.MRetailPayItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MRetailPayItemMapper extends BaseMapper<MRetailPayItem> {

    QryMRetailPayItemRespDto queryMRetailPayItem(@Param("v_retail_id") Integer v_retail_id);


}
