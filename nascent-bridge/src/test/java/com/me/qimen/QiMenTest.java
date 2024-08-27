package com.me.qimen;

import com.me.nascent.modules.qimen.service.QiMenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class QiMenTest {


    @Autowired
    QiMenService qiMenService;

    @Test
    public void putQiMenCustomer() throws Exception {
        qiMenService.putQiMenCustomer();
    }

    @Test
    public void putQiMenGrade() throws Exception {
        qiMenService.putQiMenGrade();
    }

    @Test
    public void putQiMenPoint() throws Exception {
        qiMenService.putQiMenPoint();
    }

    @Test
    public void putQiMenTrade() throws Exception{
        qiMenService.putQiMenTrade();
    }

    @Test
    public void putQiMenReFund() throws Exception{
        qiMenService.putQiMenReFund();
    }
}
