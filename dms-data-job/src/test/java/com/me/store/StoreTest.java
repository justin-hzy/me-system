package com.me.store;


import com.me.modules.fl.tw.service.TranFlInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class StoreTest {

    @Autowired
    private TranFlInventoryService tranFlInventoryService;

    @Test
    //@Transactional(rollbackFor = Exception.class)
    public void getStoreTest(){

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
