package com.me.nascent.modules.grade.service;

public interface TransGradeService {

    void putMemberTongGrade() throws Exception;

    void transMemberTongGrade(Long viewId) throws Exception;

    void transShopCustomerGrade() throws Exception;

    void putPureGrade() throws Exception;

    void checkPureGrade();
}
