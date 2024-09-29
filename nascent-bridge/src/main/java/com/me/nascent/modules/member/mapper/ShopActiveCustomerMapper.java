package com.me.nascent.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.nascent.modules.member.vo.QueryMemberVo;
import com.me.nascent.modules.member.entity.ShopActiveCustomer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopActiveCustomerMapper extends BaseMapper<ShopActiveCustomer> {

    List<QueryMemberVo> queryMemberVos(List<Long> shopIds);

    List<QueryMemberVo> queryMemberVos1(List<Long> shopIds);

    List<QueryMemberVo> queryOffLineMemberVos(List<Long> shopIds);
}
