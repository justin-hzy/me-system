package com.me.nascent.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PureMemberNickInfoMapper extends BaseMapper<PureMemberNickInfo> {

    @Select({
                    "SELECT p.mainId, p.nasOuid, p.platform ",
                    "FROM pure_member_nick_info p ",
                    "LEFT JOIN grade_customer_info g ON p.nasOuid = g.nasOuid AND p.platform = g.platform AND p.mainId = g.mainId ",
                    "WHERE g.nasOuid IS NULL and g.mainId IS NULL and g.platform IS NULL"
                })
    List<PureMemberNickInfo> findNotInGradeCustomerInfo();
}
