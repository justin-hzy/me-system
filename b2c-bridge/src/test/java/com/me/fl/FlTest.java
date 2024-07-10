package com.me.fl;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.fl.service.FlOrderService;
import com.me.modules.fl.service.FlReOrderService;
import com.me.modules.fl.service.FuLunHttpService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class FlTest {

    @Autowired
    FuLunHttpService fuLunHttpService;

    @Autowired
    FlOrderService flOrderService;

    @Autowired
    FlReOrderService flReOrderService;


    @Test
    public void test(){

    }


    @Test
    public void flOrderSend(){
        List<String> params  = flOrderService.queryOrder();
        log.info("params="+params.toString());
//        for(String param : params){
//            flOrderService.sendOrder(param);
//        }
    }

    @Test
    public void flReOrderSend(){

        List<String> params = flReOrderService.queryReOrder();
        //log.info("params="+params.toString());
//        for(String param : params){
//            flReOrderService.sendOrder(param);
//        }


    }

    /*同步库存测试单元*/
    @Test
    public void testSysStock(){

        String url = "https://upace-api.ibiza.com.tw/v1/stocks/"+"4711401210962-11111?"+"storage_type="+"A1";

        log.info("url="+url);

        JSONObject apiRes = fuLunHttpService.doGetAction(url);

    }

    @Test
    public void testSysOrder(){
        String url = "";

        String params = "";

        fuLunHttpService.doPostAction(params,url);
    }

    @Test
    public void testSysReOrder(){
        String url = "";

        String params = "";

        fuLunHttpService.doPostAction(params,url);
    }

}
