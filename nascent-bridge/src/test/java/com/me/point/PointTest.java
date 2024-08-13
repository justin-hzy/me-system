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
    public void getPurePoint() throws Exception {

        /*100000387	泊美  100000386	Za姬芮*/

        /*pcode-206261	泊美积分体系
          pcode-206256	Za线上积分体系
          pcode-206258	Za姬芮线下积分体系*/
        transPointService.transPoint("pcode-206261",100000387L);
    }
}
