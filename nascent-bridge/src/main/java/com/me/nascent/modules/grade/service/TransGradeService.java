package com.me.nascent.modules.grade.service;

import java.util.List;

public interface TransGradeService {

    void putMemberTongGrade(Long shopId) throws Exception;

    void transMemberTongGrade(Long viewId) throws Exception;

    void transShopActiveCustomerGrade(Long viewId) throws Exception;

    void transOffLineShopActiveCustomerGrade() throws Exception;

    void putShopActiveCustomerGrade(List<Long> shopId) throws Exception;

    void putOffLineShopActiveCustomerGrade(List<Long> shopIds) throws Exception;

    void putPureGrade() throws Exception;

    void checkPureGrade();


}
