package com.me.modules.order.mtransfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.mtransfer.entity.MTransfer;

public interface MTransferService extends IService<MTransfer> {

    void mTransferAc(Integer v_m_transfer_id);

    void mTransferItemAm(Integer p_id);

    void mTransSubmit(Integer p_submittedsheetid);

    void mTransferOutQtyCop(Integer p_id,Integer p_user_id);

    void mTransferOutSubmit(Integer p_submittedsheetid);

    void mTransferInQtyCop(Integer p_id,Integer p_user_id);

    void mTransferInSubmit(Integer p_submittedsheetid);
}
