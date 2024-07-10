package com.me.modules.order.mretail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.dto.QryMRetailPayItemRespDto;
import com.me.modules.order.mretail.entity.MRetailPayItem;

public interface MRetailPayItemService extends IService<MRetailPayItem> {

    QryMRetailPayItemRespDto queryMRetailPayItem(Integer v_retail_id);
}
