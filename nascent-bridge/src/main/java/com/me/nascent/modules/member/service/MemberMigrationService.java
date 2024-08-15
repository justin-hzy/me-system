package com.me.nascent.modules.member.service;

import java.util.Date;

public interface MemberMigrationService {

    void transMemberByRange(Date startDate, Date endDate) throws Exception;

    void putPureMember() throws Exception;
}
