package com.me.modules.order.trans;

import com.me.modules.order.in.entity.FlashInOrder;
import com.me.modules.order.out.entity.FlashOutOrder;

import java.io.IOException;

public interface TransService {

    void transOutOrderDtl(FlashOutOrder flashOutOrder) throws IOException;

    void transInOrderList(FlashInOrder flashInOrder) throws IOException;

    void transInOrderList(FlashInOrder flashInOrder,String tableName) throws IOException;

    void transInTrfOrderList(FlashInOrder flashInOrder,String tableName) throws IOException;

    void transOutTrfOrderList(FlashOutOrder flashOutOrder,String tableName) throws IOException;
}
