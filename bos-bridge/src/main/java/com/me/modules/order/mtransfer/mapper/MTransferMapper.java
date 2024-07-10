package com.me.modules.order.mtransfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.dto.MTransferInQtyCopReqDto;
import com.me.modules.order.dto.MTransferInSubmitReqDto;
import com.me.modules.order.dto.MTransferOutQtyCopReqDto;
import com.me.modules.order.dto.MTransferOutSubmitReqDto;
import com.me.modules.order.mtransfer.entity.MTransfer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MTransferMapper extends BaseMapper<MTransfer> {

    void mTransferAc(Integer v_m_transfer_id);

    void mTransferAm(@Param("p_id") Integer p_id);

    void mTransSubmit(@Param("p_submittedsheetid") Integer p_submittedsheetid);

    void mTransferOutQtyCop(MTransferOutQtyCopReqDto reqDto);

    void mTransferOutSubmit(MTransferOutSubmitReqDto reqDto);

    void mTransferInQtyCop(MTransferInQtyCopReqDto reqDto);

    void mTransferInSubmit(MTransferInSubmitReqDto reqDto);
}
