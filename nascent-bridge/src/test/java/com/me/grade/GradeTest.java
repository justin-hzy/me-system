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

    }
}
