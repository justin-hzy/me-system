package com.me.modules.sale.trans;

import com.me.modules.sale.in.entity.FlashInOrder;
import com.me.modules.sale.out.entity.FlashOutOrder;

import java.io.IOException;

public interface TransService {

    void transOutOrderDtl(FlashOutOrder flashOutOrder) throws IOException;

    void transInOrderList(FlashInOrder flashInOrder) throws IOException;

    void transInOrderList(FlashInOrder flashInOrder,String tableName) throws IOException;

    void transInTrfOrderList(FlashInOrder flashInOrder,String tableName) throws IOException;

    void transOutTrfOrderList(FlashOutOrder flashOutOrder,String tableName) throws IOException;
}
