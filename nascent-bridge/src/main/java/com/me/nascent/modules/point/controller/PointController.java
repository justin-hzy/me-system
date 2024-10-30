package com.me.nascent.modules.point.controller;

import com.me.nascent.modules.point.service.TransPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("Point")
@Slf4j
public class PointController {

    @Qualifier("reFundExecutor")
    @Autowired
    private ThreadPoolTaskExecutor pointExecutor;

    @Autowired
    private TransPointService transPointService;

    @PostMapping("putPoint")
    private String putPoint() throws Exception {


        transPointService.putPureMemberPointByRange("pcode-206256","703184");

        /*ExecutorService poolExecutor = pointExecutor.getThreadPoolExecutor();
        AtomicInteger count = new AtomicInteger();

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            tasks.add(() -> {
                synchronized (count) {
                    count.addAndGet(10000);
                    log.info("count=" + count);

                }
                return null;
            });
        }

        try {
            poolExecutor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during point accumulation", e);
        }*/

        return "success";
    }
}
