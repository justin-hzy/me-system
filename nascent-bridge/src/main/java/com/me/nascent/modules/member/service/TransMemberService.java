package com.me.nascent.modules.member.service;

import java.util.Date;

public interface TransMemberService {

    void TransPureMemberByRange(Date startDate, Date endDate) throws Exception;

    void TransZaMemberByRange(Date startDate, Date endDate) throws Exception;
}
