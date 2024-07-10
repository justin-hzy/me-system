package com.me.modules.order.mretail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.dto.MRetailSubmitReqDto;
import com.me.modules.order.mretail.entity.MRetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MRetailMapper extends BaseMapper<MRetail> {

    Integer queryCount1(String orderNo);

    Integer queryCount2(String orderNo);

    void mRetailAc(@Param("p_id") Integer p_id);

    void updateMRetail(@Param("v_retail_id") Integer v_retail_id);

    void mRetailSubmit(MRetailSubmitReqDto mRetailSubmitReqDto);

}
