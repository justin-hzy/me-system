package com.me.nascent.modules.member.service;

import java.util.Date;
import java.util.Map;

public interface TransMemberService {

    void TransPureMemberByRange(Date startDate, Date endDate) throws Exception;

    void TransZaMemberByRange(Date startDate, Date endDate) throws Exception;

    void transPureStoreMemberByRange(Date startDate, Date endDate,Long shopId) throws Exception;

    Map<String, Object> transMemberTong(Date start, Date end, Integer pageNo) throws Exception;
}
