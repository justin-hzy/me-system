package com.me.nascent.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.member.entity.ZaMemberNickInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ZaMemberNickInfoMapper extends BaseMapper<ZaMemberNickInfo> {

    @Select({
            "SELECT p.mainId, p.nasOuid, p.platform ",
            "FROM za_member_nick_info p ",
            "LEFT JOIN za_online_member_point g ON p.nasOuid = g.nasOuid AND p.platform = g.platform",
            "WHERE g.nasOuid IS NULL and g.platform IS NULL"
    })
    List<ZaMemberNickInfo> findNotInZaOnlineCustomerInfo();

    @Select({
            "SELECT p.mainId, p.nasOuid, p.platform ",
            "FROM za_member_nick_info p ",
            "LEFT JOIN za_offline_member_point g ON p.nasOuid = g.nasOuid AND p.platform = g.platform",
            "WHERE g.nasOuid IS NULL and g.platform IS NULL"
    })
    List<ZaMemberNickInfo> findNotInZaOffCustomerInfo();
}
