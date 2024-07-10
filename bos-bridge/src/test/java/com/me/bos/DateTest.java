package com.me.bos;

import com.me.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Slf4j
public class DateTest {

    @Test
    public void getMonthDate(){
        log.info(DateUtils.getMonthStartDate());
        log.info(DateUtils.getMonthEndDate());

        int i = Integer.parseInt(null);
        System.out.println(i!=-1);
    }



    @Test
    public void getBeforeDate(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String todayString = today.format(formatter);
        String yesterdayString = yesterday.format(formatter);

        //System.out.println("今天的日期：" + todayString);
        System.out.println("昨天的日期：" + yesterdayString);
    }


    @Test
    public void getDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayString = today.format(formatter);

        System.out.println("今天的日期：" + todayString);
    }

    private static String formatDate(java.util.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
