package com.me.modules.order.mtransfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.mtransfer.dto.QryStockInMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.dto.QryStockOutMTransferItemDataRespDto;
import com.me.modules.order.mtransfer.entity.MTransferItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MTransferItemMapper extends BaseMapper<MTransferItem> {

    List<QryStockOutMTransferItemDataRespDto> queryStockOutMTransferItemData(@Param("stockOutId") String stockOutId);

    List<QryStockInMTransferItemDataRespDto> queryStockInMTransferItemData(@Param("stockInId") String stockInId);

    void updateMTransferItem(@Param("v_m_transfer_id") Integer v_m_transfer_id);

    void mTransferItemAcm(@Param("p_id") Integer p_id);



}
