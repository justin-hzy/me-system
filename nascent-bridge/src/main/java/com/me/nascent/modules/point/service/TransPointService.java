package com.me.nascent.modules.point.service;

public interface TransPointService {

    void transPurePoint(String integralAccount,Long viewId) throws Exception;

    void transZaOnlinePoint(String integralAccount,Long viewId) throws Exception;

    void getZaOfflinePoint(String integralAccount,Long viewId) throws Exception;

    void putPureMemberPoint() throws Exception;
}
