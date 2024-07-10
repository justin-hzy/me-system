package com.me.modules.order.mtransfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.mtransfer.dto.QryStockInMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.dto.QryStockOutMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.entity.MTransferItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MTransferItemService extends IService<MTransferItem> {

    List<QryStockOutMTransferItemDataRespDto> queryStockOutMTransferItemData(String stockOutId);

    List<QryStockInMTransferItemDataRespDto> queryStockInMTransferItemData(String stockInId);

    void updateMTransferItem(Integer v_m_transfer_id);

    void mTransferItemAcm(Integer p_id);


}
