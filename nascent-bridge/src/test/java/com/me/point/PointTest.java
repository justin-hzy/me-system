package com.me.point;

import com.me.nascent.modules.point.service.TransPointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PointTest {

    @Autowired
    private TransPointService transPointService;

    @Test
    public void getRePoint() throws Exception {
        transPointService.transPoint();
    }
}
