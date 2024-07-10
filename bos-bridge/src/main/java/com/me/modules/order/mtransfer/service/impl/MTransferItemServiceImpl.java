package com.me.modules.order.mtransfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.mtransfer.dto.QryStockInMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.dto.QryStockOutMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.entity.MTransferItem;
import com.me.modules.order.mtransfer.mapper.MTransferItemMapper;
import com.me.modules.order.mtransfer.service.MTransferItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MTransferItemServiceImpl extends ServiceImpl<MTransferItemMapper,MTransferItem> implements MTransferItemService {

    private MTransferItemMapper mTransferItemMapper;


    @Override
    public List<QryStockOutMTransferItemDataRespDto> queryStockOutMTransferItemData(String stockOutId) {
        return mTransferItemMapper.queryStockOutMTransferItemData(stockOutId);
    }

    @Override
    public List<QryStockInMTransferItemDataRespDto> queryStockInMTransferItemData(String stockInId) {
        return mTransferItemMapper.queryStockInMTransferItemData(stockInId);
    }

    @Override
    public void updateMTransferItem(Integer v_m_transfer_id) {
        mTransferItemMapper.updateMTransferItem(v_m_transfer_id);
    }

    @Override
    public void mTransferItemAcm(Integer p_id) {
        mTransferItemMapper.mTransferItemAcm(p_id);
    }


}
