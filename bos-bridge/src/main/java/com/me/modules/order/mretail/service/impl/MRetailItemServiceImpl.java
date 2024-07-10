package com.me.modules.order.mretail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.mretail.dto.QryMRetailItemRespDto;
import com.me.modules.order.mretail.entity.MRetailItem;
import com.me.modules.order.mretail.mapper.MRetailItemMapper;
import com.me.modules.order.mretail.service.MRetailItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MRetailItemServiceImpl extends ServiceImpl<MRetailItemMapper, MRetailItem> implements MRetailItemService {

    private MRetailItemMapper mRetailItemMapper;

    @Override
    public List<QryMRetailItemRespDto> queryMRetailItem(String stockOutId) {
        return mRetailItemMapper.queryMRetailItem(stockOutId);
    }

    @Override
    public void updateMRetailItem(Integer v_retail_id) {
        mRetailItemMapper.updateMRetailItem(v_retail_id);
    }

}
