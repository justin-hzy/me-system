package com.me.nascent.modules.member.service;

import java.util.Date;

public interface MemberMigrationService {

    void transPureMemberByRange(Date startDate, Date endDate) throws Exception;

    void transZaMemberByRange(Date startDate, Date endDate) throws Exception;

    void transPureStoreMemberByRange(Date startDate, Date endDate,Long shopId) throws Exception;


    void transMemberTong(Date start,Date end) throws Exception;

    void putPureMember() throws Exception;

    void putMemberTong() throws Exception;
}
