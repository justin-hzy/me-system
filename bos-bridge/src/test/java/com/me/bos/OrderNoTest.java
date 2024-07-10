package com.me.bos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
public class OrderNoTest {

    @Test
    public void orderNoTest(){
        //提取海外旺店通赠品平台订单号
        String input = "GI578599874787379294-AT202403030390";
        if(input.contains("-")){
            String result = input.split("-")[0];
            System.out.println(result);

            String pattern = "\\d+"; // 匹配连续的数字

            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(input);

            if (matcher.find()) {
                result = matcher.group();
                System.out.println(result);
            }
        }
    }
}
