package com.me.nascent.modules.point.service;

import java.util.Date;

public interface TransPointService {

    void transPurePoint(String integralAccount,Long viewId) throws Exception;

    void transZaOnlinePoint(String integralAccount,Long viewId) throws Exception;

    void getZaOfflinePoint(String integralAccount,Long viewId) throws Exception;

    void putPureMemberPoint() throws Exception;

    void transMemberPoint(Date start, Date end,String integralAccount) throws Exception;

    void testQueryPost() throws Exception;
}
