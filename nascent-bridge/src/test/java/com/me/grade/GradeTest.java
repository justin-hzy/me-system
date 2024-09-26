package com.me.grade;

import com.me.nascent.modules.grade.service.TransGradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GradeTest {

    @Autowired
    TransGradeService transGradeService;



    @Test
    public void transGradeTest() throws Exception {

        //100000387 泊美  100000386	Za姬芮
        Long viewId = 100000387L;


    }

    @Test
    public void transZaGradeTest() throws Exception {

        //100000387 泊美  100000386	Za姬芮
        Long viewId = 100000386L;


    }


    @Test
    public void transShopCustomerGradeTest() throws Exception {
        transGradeService.transShopCustomerGrade();
    }

    @Test
    public void checkPureGrade(){
        //transGradeService.checkPureGrade();
    }


    @Test
    public void putGrade() throws Exception {
        transGradeService.putPureGrade();
    }


    @Test
    public void putMemberTongGrade() throws Exception {
        //100000387 泊美  100000386	Za姬芮
        Long viewId = 100000387L;
        transGradeService.putMemberTongGrade();
    }

    @Test
    public void transMemberTongGrade() throws Exception {
        //100000387 泊美  100000386	Za姬芮
        Long viewId = 100000387L;
        transGradeService.transMemberTongGrade(viewId);
    }
}
