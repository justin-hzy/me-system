package com.me.nascent.modules.reorder.controller;

import com.me.nascent.modules.reorder.service.TransReOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("ReFund")
@Slf4j
public class ReFundController {

    @Qualifier("reFundExecutor")
    @Autowired
    private ThreadPoolTaskExecutor reFundExecutor;


    @Autowired
    private TransReOrderService transReOrderService;

    @PostMapping("getReFundByRange")
    private String getReFundByRange() throws Exception {
        String year = "2023";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();


        ExecutorService reFundExecutorService = reFundExecutor.getThreadPoolExecutor();

        for (int month = 3; month <= 8; month++) {
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
            data(startDate,endDate);
            /*reFundExecutorService.execute(()->{
                //System.out.println(Thread.currentThread().getName());
                log.info(Thread.currentThread().getName()+"同步订单数据: " + startStr + " 到 " + endStr);
                try {
                    data(startDate,endDate);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });*/

            /*CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {

                log.info("同步订单数据: " + startStr + " 到 " + endStr);
                try {
                    data(startDate,endDate);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return "success";
            },reFundExecutor);*/
        }
        return "success";
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
            log.info(Thread.currentThread().getName()+"时间范围: " + startStr + " 到 " + endStr);

            transReOrderService.transReOrder(startDate,endDateOfWeek);
            startDate = endDateOfWeek;
        }

    }
}
