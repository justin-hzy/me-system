package com.me.modules.order.mretail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.dto.MRetailSubmitReqDto;
import com.me.modules.order.mretail.entity.MRetail;
import com.me.modules.order.mretail.mapper.MRetailMapper;
import com.me.modules.order.mretail.service.MRetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MRetailServiceImpl extends ServiceImpl<MRetailMapper, MRetail> implements MRetailService {

    private MRetailMapper mRetailMapper;


    @Override
    public Integer queryCount1(String orderNo) {
        return mRetailMapper.queryCount1(orderNo);
    }

    @Override
    public Integer queryCount2(String orderNo) {
        return mRetailMapper.queryCount2(orderNo);
    }

    @Override
    public void mRetailAc(Integer p_id) {
        /*MRetailAcReqDto reqDto = new MRetailAcReqDto();
        reqDto.setP_id(p_id);*/
        mRetailMapper.mRetailAc(p_id);
    }

    @Override
    public void updateMRetail(Integer v_retail_id) {
        mRetailMapper.updateMRetail(v_retail_id);
    }

    @Override
    public void mRetailSubmit(Integer v_retail_id) {
        MRetailSubmitReqDto reqDto = new MRetailSubmitReqDto();
        reqDto.setP_submittedsheetid(v_retail_id);
        mRetailMapper.mRetailSubmit(reqDto);
    }
}
