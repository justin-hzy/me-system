package com.me.modules.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.transaction.entity.TransactionDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionDetailMapper extends BaseMapper<TransactionDetail> {
}
