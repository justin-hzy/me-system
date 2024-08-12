package com.me.reorder;

import com.me.nascent.modules.reorder.service.TransReOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
public class ReFundTest {

    @Autowired
    private TransReOrderService transReOrderService;


    @Qualifier("reFundExecutor")
    @Autowired
    private ThreadPoolTaskExecutor reFundExecutor;

    @Test
    public void getReOrder() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date startDate = sdf.parse("2023-07-01 00:00:00");
            Date endDate = sdf.parse("2023-07-31 23:59:59");

            while (startDate.before(endDate)) {
                Date endDateOfWeek = new Date(startDate.getTime() +  30 * 60 * 1000); // 一小时
                if (endDateOfWeek.after(endDate)) {
                    endDateOfWeek = endDate;
                }

                String startStr = sdf.format(startDate);
                String endStr = sdf.format(endDateOfWeek);
                System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

                transReOrderService.transReOrder(startDate,endDateOfWeek);
                startDate = endDateOfWeek;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void saveReOrder() throws Exception {
        transReOrderService.putReOrder();

    }

    @Test
    public void executorsTest(){
        String year = "2023";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();


        ExecutorService reFundExecutorService = reFundExecutor.getThreadPoolExecutor();

        for (int month = 7; month <= 7; month++) {
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startDate = cal.getTime();
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endDate = cal.getTime();
            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDate);
            /*reFundExecutorService.execute(()->{
                //System.out.println(Thread.currentThread().getName());
                log.info("同步订单数据: " + startStr + " 到 " + endStr);
                data(startDate,endDate);
            });*/

            CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {

                log.info("同步订单数据: " + startStr + " 到 " + endStr);
                try {
                    data(startDate,endDate);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return "success";
            },reFundExecutor);
        }
    }

    private void data(Date startDate,Date endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (startDate.before(endDate)) {
            Date endDateOfWeek = new Date(startDate.getTime() +  30 * 60 * 1000); // 一小时
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDateOfWeek);
            log.info("时间范围: " + startStr + " 到 " + endStr);

            transReOrderService.transReOrder(startDate,endDateOfWeek);
            startDate = endDateOfWeek;
        }

    }

    @Test
    public void test(){

        int year = 2024;
        int month = 6;

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        LocalDate startDate = firstDayOfMonth;
        LocalDate endDate = startDate.plusDays(3);

        while (startDate.isBefore(lastDayOfMonth)) {
            System.out.println(startDate + " - " + endDate);

            startDate = endDate.plusDays(1);
            endDate = startDate.plusDays(3);

            if(endDate.isAfter(lastDayOfMonth)){
                endDate = lastDayOfMonth;
            }

        }
        /*String year = "2023";
        int month = 6;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date endDate = cal.getTime();

        String startStr = sdf.format(startDate);
        String endStr = sdf.format(endDate);




        log.info(Thread.currentThread().getName()+"同步订单数据: " + startStr + " 到 " + endStr);*/

    }
}
