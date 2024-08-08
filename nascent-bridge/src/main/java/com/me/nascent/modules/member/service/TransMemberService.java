package com.me.nascent.modules.member.service;

import java.util.Date;

public interface TransMemberService {

    void TransMemberByRange(Date startDate, Date endDate) throws Exception;
}
