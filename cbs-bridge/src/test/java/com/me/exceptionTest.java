package com.me;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class exceptionTest {

    @Test
    public void one(){
        try{
            throw new NullPointerException();
        }catch (Exception e){
           log.info(e.getMessage());
        }
    }
}
