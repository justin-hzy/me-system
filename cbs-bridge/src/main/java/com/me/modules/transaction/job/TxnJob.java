package com.me.modules.transaction.job;

import com.me.modules.transaction.service.TxnService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class TxnJob {

    private TxnService txnService;

    /*获取上一天的数据*/
    @Scheduled(cron = "0 0 9 * * ? ")
    protected void getTmrTxnJob() throws IOException {
        log.info("getTmrTxnJob开始执行，获取上一天的数据");
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yesterdayString = yesterday.format(formatter);



        Integer currentPage = 1;
        Map<String,Integer> respMap = txnService.queryTxn(currentPage,yesterdayString);
        Integer nextPage = respMap.get("nextPage");

        while(nextPage !=0){
            respMap = txnService.queryTxn(nextPage,yesterdayString);
            nextPage = respMap.get("nextPage");
            log.info("nextPage="+nextPage);
        }
        log.info("getTmrTxnJob执行结束");
    }


    @Scheduled(cron = "0 0/59 10-19 * * ?")
    protected void getTodayTxnJob() throws IOException {
        log.info("getTodayTxnJob开始执行，获取当天的数据");
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayString = today.format(formatter);


        Integer currentPage = 1;
        Map<String,Integer> respMap = txnService.queryTxn(currentPage,todayString);
        Integer nextPage = respMap.get("nextPage");

        while(nextPage !=0){
            respMap = txnService.queryTxn(nextPage,todayString);
            nextPage = respMap.get("nextPage");
            log.info("nextPage="+nextPage);
        }

        log.info("getTodayTxnJob执行结束");
    }
}
