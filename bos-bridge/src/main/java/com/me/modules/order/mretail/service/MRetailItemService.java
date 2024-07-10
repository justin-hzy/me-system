package com.me.modules.order.mretail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.mretail.dto.QryMRetailItemRespDto;
import com.me.modules.order.mretail.entity.MRetailItem;

import java.util.List;

public interface MRetailItemService extends IService<MRetailItem> {

    List<QryMRetailItemRespDto> queryMRetailItem(String stockOutId);

    void updateMRetailItem(Integer v_retail_id);

}
