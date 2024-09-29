package com.me.nascent.modules.member.service;

import java.util.Date;
import java.util.List;

public interface MemberMigrationService {

    void transPureMemberByRange(Date startDate, Date endDate) throws Exception;

    void transZaMemberByRange(Date startDate, Date endDate) throws Exception;

    void transStoreMemberByRange(Date startDate, Date endDate,Long shopId) throws Exception;


    void transMemberTong(Date start,Date end) throws Exception;

    void putOnLineShopActiveCustomer() throws Exception;

    void putOffLineShopActiveCustomer(List<Long> shopIds) throws Exception;

    void putMemberTong() throws Exception;
}
