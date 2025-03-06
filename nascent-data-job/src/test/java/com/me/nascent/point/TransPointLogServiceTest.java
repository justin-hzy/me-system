package com.me.nascent.point;

import com.me.modules.nascent.point.service.TransPointLogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class TransPointLogServiceTest {

    @Autowired
    TransPointLogService transPointLogService;

    @Test
    void transPointLogQuery() throws Exception {
        List<String> providerGUIDs = new ArrayList<>();

        //线上店铺guid
        //淘宝：za姬芮官方旗舰店，providerGUID
        //providerGUIDs.add("a3ea97e1-2395-11e3-b2d9-00163e012052");
        //有赞：Za姬芮官方旗舰店，providerGUID 存在昵称为空的情况
        //providerGUIDs.add("5e550fb3-dfd2-4cee-bf56-ac445170b43f");
        //抖店：Za姬芮官方旗舰店，providerGUID 存在昵称为空的情况 大概是10个
        //providerGUIDs.add("ac75d87a-56e7-483b-b323-df90e19bdd96");
        //会俱：za会员俱乐部，providerGUID
        //providerGUIDs.add("8498fddb-f424-4fcd-b46c-7e09dff5852f");


        String a = "2024-10-01";
        String b = "2024-12-31";

        List<String> dateList = getDateRange(a, b);
        for (String date : dateList) {
            /*for (int i = 0; i < 144; i++) { // 144个10分钟
                int hour = i / 6;
                int minute = (i % 6) * 10;

                String startTime = String.format("%s %02d:%02d:%02d", date, hour, minute, 0);
                String endTime = String.format("%s %02d:%02d:%02d", date, hour, minute + 10, 0);

                // 处理分钟溢出
                if (minute + 10 >= 60) {
                    endTime = String.format("%s %02d:%02d:%02d", date, hour + 1, (minute + 10) % 60, 0);
                }

                log.info("开始时间: " + startTime + ", 结束时间: " + endTime);

                Long nextId = 0L;
                Boolean isNext = true;

                while (isNext){
                        Map<String,Object> respMap = transPointLogService.transPointLog(startTime,endTime,nextId);
                    isNext = (Boolean) respMap.get("isNext");
                    nextId = (Long) respMap.get("nextId");
                }
            }*/

            for (int i = 0; i < 288; i++) { // 288个5分钟
                int hour = i / 12; // 每小时12个5分钟
                int minute = (i % 12) * 5;

                String startTime = String.format("%s %02d:%02d:%02d", date, hour, minute, 0);
                String endTime;

                // 处理分钟溢出
                if (minute + 5 >= 60) {
                    hour += 1; // 小时加1
                    minute = (minute + 5) % 60; // 计算新的分钟
                } else {
                    minute += 5; // 直接加5分钟
                }

                // 处理小时溢出
                if (hour >= 24) {
                    hour = 0; // 重置小时
                    // 这里可以使用日期处理库来增加一天
                    LocalDate localDate = LocalDate.parse(date);
                    localDate = localDate.plusDays(1);
                    date = localDate.toString(); // 更新日期
                }

                endTime = String.format("%s %02d:%02d:%02d", date, hour, minute, 0);

                log.info("开始时间: " + startTime + ", 结束时间: " + endTime);

                Long nextId = 1L;
                Boolean isNext = true;

                while (isNext){
                    Map<String,Object> respMap = transPointLogService.transPointLog(startTime,endTime,nextId);
                    isNext = (Boolean) respMap.get("isNext");
                    nextId = (Long) respMap.get("nextId");
                }
            }


            /*for (int i = 0; i < 86400; i++) { // 86400个1秒
                int hour = i / 3600; // 每小时3600个1秒
                int minute = (i % 3600) / 60; // 每分钟60个1秒
                int second = i % 60; // 秒

                String startTime = String.format("%s %02d:%02d:%02d", date, hour, minute, second);
                String endTime = String.format("%s %02d:%02d:%02d", date, hour, minute, second + 1);

                // 处理秒溢出
                if (second + 1 >= 60) {
                    endTime = String.format("%s %02d:%02d:%02d", date, hour, minute + 1, 0);
                    // 处理分钟溢出
                    if (minute + 1 >= 60) {
                        endTime = String.format("%s %02d:%02d:%02d", date, hour + 1, 0, 0);
                        // 处理小时溢出
                        if (hour + 1 >= 24) {
                            // 处理日期溢出
                            LocalDateTime localDateTime = LocalDateTime.parse(date + "T00:00:00");
                            localDateTime = localDateTime.plusDays(1);
                            endTime = String.format("%s %02d:%02d:%02d", localDateTime.toLocalDate(), 0, 0, 0);
                        }
                    }
                }

                log.info("开始时间: " + startTime + ", 结束时间: " + endTime);

                Long nextId = 0L;
                Boolean isNext = true;

                while (isNext){
                    Map<String,Object> respMap = transPointLogService.transPointLog(startTime,endTime,nextId);
                    isNext = (Boolean) respMap.get("isNext");
                    nextId = (Long) respMap.get("nextId");
                }
            }*/
        }

        /*String startDateStr  = "2024-10-14 15:00:00";
        String endDateStr = "2024-10-14 15:05:00";

        Long nextId = 1L;
        Boolean isNext = true;

        while (isNext){
            Map<String,Object> respMap = transPointLogService.transPointLog(startDateStr,endDateStr,nextId);
            isNext = (Boolean) respMap.get("isNext");
            nextId = (Long) respMap.get("nextId");
        }*/
    }

    public List<String> getDateRange(String startDate, String endDate) {
        List<String> dateList = new ArrayList<>();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        while (!start.isAfter(end)) {
            dateList.add(start.toString());
            start = start.plusDays(1);
        }

        List<String> formattedDateList = new ArrayList<>();
        for (String date : dateList) {
            formattedDateList.add(LocalDate.parse(date).toString());
        }

        return formattedDateList;
    }


}
