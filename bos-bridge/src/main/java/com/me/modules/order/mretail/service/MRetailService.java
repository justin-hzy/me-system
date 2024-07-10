package com.me.modules.order.mretail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.mretail.entity.MRetail;
import org.apache.ibatis.annotations.Param;

public interface MRetailService extends IService<MRetail> {

    Integer queryCount1(@Param("orderNo") String orderNo);

    Integer queryCount2(@Param("orderNo") String orderNo);

    void mRetailAc(Integer p_id);

    void updateMRetail(Integer v_retail_id);

    void mRetailSubmit(Integer v_retail_id);
}
