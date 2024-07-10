package com.me.modules.bi.job;

import com.me.common.annotation.Elk;
import com.me.modules.bi.service.ByteNewEumService;
import com.me.modules.bi.service.TransByteNewDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class BiJob {

    private TransByteNewDataService transByteNewDataService;

    private ByteNewEumService byteNewEumService;

    @Elk
    @Scheduled(cron = "0 0/10 9-23 * * ? ")
    protected void getStoreAfSalesException(){
        log.info("初始化仓库售后异常登记表枚举数据开始...");
        byteNewEumService.initStoreAfSalesException();
        log.info("初始化仓库售后异常登记表枚举数据结束...");

        log.info("初始化店铺数据开始...");
        byteNewEumService.initShop();
        log.info("初始化店铺数据结束...");

        log.info("初始化用户数据开始...");
        byteNewEumService.initUser();
        log.info("初始化用户数据结束...");

        log.info("仓库售后异常登记表同步开始...");
        LocalDateTime begin =  LocalDateTime.now();
        Map<String,Integer> map = transByteNewDataService.transStoreAfSalesException();
        Integer totalPageNum = map.get("total_page_num");
        for(int i=2;i<=totalPageNum;i++){
            String pageNum = String.valueOf(i);
            transByteNewDataService.transStoreAfSalesException(pageNum);
        }
        log.info("仓库售后异常登记表同步完结...");


        log.info("仓库售后异常登记表回收站同步开始...");
        Map<String,Integer> map1 =transByteNewDataService.transRecycleStoreAfSalesException();
        Integer totalPageNum1 = map1.get("total_page_num");
        System.out.println("total_page_num="+map1.get("total_page_num"));
        for(int i=2;i<=totalPageNum1;i++){
            String pageNum = String.valueOf(i);
            System.out.println("pageNum="+pageNum);
            transByteNewDataService.transRecycleStoreAfSalesException(pageNum);
        }
        log.info("仓库售后异常登记表回收站同步完结...");
        LocalDateTime end =  LocalDateTime.now();
        log.info("耗时:"+(String.valueOf(end.getMinute()-begin.getMinute())));
    }

    @Elk
    @Scheduled(cron = "0 0/3 9-23 * * ? ")
    protected void getAllergyReactionOrder(){

        log.info("初始化过敏订单数据开始...");
        byteNewEumService.initAllergyReactionOrder();
        log.info("初始化过敏订单数据结束...");

        log.info("过敏订单登记表同步开始...");
        Map<String,Integer> map = transByteNewDataService.transAllergyReactionOrder();
        Integer totalPageNum = map.get("total_page_num");
        System.out.println("totalPageNum="+totalPageNum);
        for(int i=2;i<=totalPageNum;i++){
            String pageNum = String.valueOf(i);
            transByteNewDataService.transAllergyReactionOrder(pageNum);
        }
        log.info("过敏订单登记表同步完结...");

        log.info("过敏订单登记表回收站同步开始...");
        Map<String,Integer> map1 =transByteNewDataService.transRecycleAllergyReactionOrder();
        Integer totalPageNum1 = map1.get("total_page_num");
        System.out.println("totalPageNum="+totalPageNum1);
        for(int i=2;i<=totalPageNum1;i++){
            String pageNum = String.valueOf(i);
            transByteNewDataService.transRecycleAllergyReactionOrder(pageNum);
        }
        log.info("过敏订单登记表回收站同步完结...");
    }

    @Elk
    @Scheduled(cron = "0 0/30 9-23 * * ? ")
    protected void getReissueOrder(){
        log.info("初始化补发单枚举数据开始...");
        byteNewEumService.initReissueOrder();
        log.info("初始化补发单枚举数据结束...");

        log.info("补发单同步开始...");
        Map<String,Integer> map = transByteNewDataService.transReissueOrder();
        Integer totalPageNum = map.get("total_page_num");
        for(int i=2;i<=totalPageNum;i++){
            String pageNum = String.valueOf(i);
            transByteNewDataService.transReissueOrder(pageNum);
        }
        log.info("补发单同步结束...");

        log.info("过敏订单登记表回收站同步开始...");
        Map<String,Integer> map1 = transByteNewDataService.transRecycleTransReissueOrder();
        Integer totalPageNum1 = map1.get("total_page_num");
        for(int i=2;i<=totalPageNum1;i++){
            String pageNum = String.valueOf(i);
            transByteNewDataService.transRecycleTransReissueOrder(pageNum);
        }
        log.info("过敏订单登记表回收站同步完结...");
    }
}
