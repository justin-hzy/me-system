package com.me.reorder;

import com.me.nascent.modules.reorder.service.TransReOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootTest
@Slf4j
public class ReFundTest {

    @Autowired
    private TransReOrderService transReOrderService;

    @Test
    public void getReOrder() throws Exception {

        String startDateStr  = "2024-08-07 00:00:00";

        //定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 解析字符串到 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, formatter);
        // 转换为 Date
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime oneHourLater = startDateTime.plusHours(1);

        // 将 LocalDateTime 转换为 Date
        Date oneHourLaterDate = Date.from(oneHourLater.atZone(ZoneId.systemDefault()).toInstant());
        Long nextId = 0L;

        transReOrderService.transReOrder(0L,startDate,oneHourLaterDate);
    }


    @Test
    public void saveReOrder() throws Exception {
        transReOrderService.putReOrder();

    }
}
