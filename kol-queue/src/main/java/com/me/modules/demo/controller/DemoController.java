package com.me.modules.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.modules.bi.entity.*;
import com.me.modules.bi.service.*;
import com.me.modules.http.service.ByteNewHttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("demo")
@AllArgsConstructor
@Slf4j
public class DemoController {

    private ByteNewHttpService byteNewHttpService;

    private IssueTypeService issueTypeService;

    private TransByteNewDataService transByteNewDataService;

    private ByteNewEumService byteNewEumService;

    //获取枚举
    final String app_key = "3331225291";
    final String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
    //359 仓库售后异常等级表  558 过敏订单 1349 补发单
    final String project_id = "359";
    final String app_secret = "71dc01248f11697674f3e610971c3013";

    @GetMapping("getStoreAfSalesException")
    public String getStoreAfSalesException(){
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
        return "success";
    }

    @GetMapping("getRecycleData")
    public String getRecycleData(){
        return "success";
    }


    @GetMapping("getAllergyReactionOrder")
    public String getAllergyReactionOrder(){


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
        return "";
    }

    @GetMapping("getUserData")
    public String getUserData() throws IOException {

        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method","user.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        String base_url = "https://open.bytenew.com/gateway/api/user";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");
        System.out.println("full_url="+full_url);

        // 设置请求体参数
        Map<String,String> body = new HashMap<>();
        body.put("pageSize","1000");
        body.put("pageNum","1");

        String responseBody = byteNewHttpService.send_post_request(full_url,params);
        System.out.println(responseBody);
        return "success";
    }

    @GetMapping("testDb")
    public String testDb(){
        List<IssueType> list = issueTypeService.list();
        System.out.println(list.toString());
        return "testDb";
    }
}
