package com.me.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
@Slf4j
public class CommonTest {

    @Test
    void commonTest(){
        System.out.println(Integer.compare(Integer.valueOf(-5),Integer.valueOf(1)));

        String aa = "-1";
    }
}
