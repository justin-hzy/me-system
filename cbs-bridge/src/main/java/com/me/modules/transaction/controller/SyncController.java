package com.me.modules.transaction.controller;

import com.me.modules.transaction.service.TxnService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("sync")
@AllArgsConstructor
@Slf4j
public class SyncController {


    private TxnService txnService;

    @GetMapping("syncHistory")
    public void syncHistory() throws IOException {
        String historydayString = "2024-04-18";

        Integer currentPage = 1;
        Map<String,Integer> respMap = txnService.queryTxn(currentPage,historydayString);
        Integer nextPage = respMap.get("nextPage");

        while(nextPage !=0){
            respMap = txnService.queryTxn(nextPage,historydayString);
            nextPage = respMap.get("nextPage");
            log.info("nextPage="+nextPage);
        }
        log.info("getTmrTxnJob执行结束");
    }

}
