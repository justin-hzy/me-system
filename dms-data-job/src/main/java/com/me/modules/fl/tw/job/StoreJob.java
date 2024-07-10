package com.me.modules.fl.tw.job;



import com.me.modules.fl.tw.service.TranFlInventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class StoreJob {

    private TranFlInventoryService tranFlInventoryService;

    //@Elk
    @Scheduled(cron = "0 0 0/2 * * *")
    public void getStore(){

        Integer page = 0;

        boolean flag = true;

        //tranFlInventoryService.tranFlInventoryByPage(Integer.toString(page));

        while (flag){
            log.info("page="+page);
            flag = tranFlInventoryService.tranFlInventoryByPage(Integer.toString(page));
            page = page + 1;
        }
    }
}
