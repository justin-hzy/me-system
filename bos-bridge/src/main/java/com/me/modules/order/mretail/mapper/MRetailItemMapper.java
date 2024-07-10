package com.me.modules.order.mretail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.mretail.dto.QryMRetailItemRespDto;
import com.me.modules.order.mretail.entity.MRetailItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MRetailItemMapper extends BaseMapper<MRetailItem> {

    List<QryMRetailItemRespDto> queryMRetailItem(@Param("stockOutId") String stockOutId);

    void updateMRetailItem(@Param("v_retail_id") Integer v_retail_id);



}
