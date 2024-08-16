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
        Long viewId = 100000386L;

        transGradeService.TransZaGrade(viewId);
    }


    @Test
    public void putGrade() throws Exception {
        transGradeService.putGrade();
    }
}
