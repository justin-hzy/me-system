package com.me.modules.order.mtransfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.dto.MTransferInQtyCopReqDto;
import com.me.modules.order.dto.MTransferInSubmitReqDto;
import com.me.modules.order.dto.MTransferOutQtyCopReqDto;
import com.me.modules.order.dto.MTransferOutSubmitReqDto;
import com.me.modules.order.mtransfer.entity.MTransfer;
import com.me.modules.order.mtransfer.mapper.MTransferMapper;
import com.me.modules.order.mtransfer.service.MTransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MTransferServiceImpl extends ServiceImpl<MTransferMapper, MTransfer> implements MTransferService {

    private MTransferMapper mTransferMapper;

    @Override
    public void mTransferAc(Integer v_m_transfer_id) {
        mTransferMapper.mTransferAc(v_m_transfer_id);
    }

    @Override
    public void mTransferItemAm(Integer p_id) {
        mTransferMapper.mTransferAm(p_id);
    }

    @Override
    public void mTransSubmit(Integer p_submittedsheetid) {
        mTransferMapper.mTransSubmit(p_submittedsheetid);
    }

    @Override
    public void mTransferOutQtyCop(Integer p_id, Integer p_user_id) {
        MTransferOutQtyCopReqDto reqDto = new MTransferOutQtyCopReqDto();
        reqDto.setP_id(p_id);
        reqDto.setP_user_id(893);
        mTransferMapper.mTransferOutQtyCop(reqDto);
    }

    @Override
    public void mTransferOutSubmit(Integer p_submittedsheetid) {
        MTransferOutSubmitReqDto reqDto = new MTransferOutSubmitReqDto();
        reqDto.setP_submittedsheetid(p_submittedsheetid);
        mTransferMapper.mTransferOutSubmit(reqDto);
    }

    @Override
    public void mTransferInQtyCop(Integer p_id, Integer p_user_id) {
        MTransferInQtyCopReqDto reqDto = new MTransferInQtyCopReqDto();
        reqDto.setP_id(p_id);
        reqDto.setP_user_id(893);
        mTransferMapper.mTransferInQtyCop(reqDto);
    }

    @Override
    public void mTransferInSubmit(Integer p_submittedsheetid) {
        MTransferInSubmitReqDto reqDto = new MTransferInSubmitReqDto();
        reqDto.setP_submittedsheetid(p_submittedsheetid);
        mTransferMapper.mTransferInSubmit(reqDto);
    }
}
