package com.me.modules.order.mretail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.dto.QryMRetailPayItemRespDto;
import com.me.modules.order.mretail.entity.MRetailPayItem;
import com.me.modules.order.mretail.mapper.MRetailPayItemMapper;
import com.me.modules.order.mretail.service.MRetailPayItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MRetailPayItemServiceImpl extends ServiceImpl<MRetailPayItemMapper, MRetailPayItem> implements MRetailPayItemService {

    private MRetailPayItemMapper mRetailPayItemMapper;

    @Override
    public QryMRetailPayItemRespDto queryMRetailPayItem(Integer v_retail_id) {
        return mRetailPayItemMapper.queryMRetailPayItem(v_retail_id);
    }




}
