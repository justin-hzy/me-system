package com.me.modules.dms.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.core.JsonResult;
import com.me.modules.dms.sys.entity.Order;
import com.me.modules.dms.sys.service.ClientService;
import com.me.modules.dms.sys.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("order")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class OrderController {

    private OrderService orderService;


    @PostMapping("checkOrder")
    private JsonResult checkOrder(@RequestBody String orderIds){
        JSONObject jsonObject = JSONObject.parseObject(orderIds);
        orderIds = jsonObject.getString("field14839");
        List<String> orderIdList = Arrays.asList(orderIds.split(","));
        Map<String,String> orderIdMap = new HashMap<>();
        for (String orderId : orderIdList){
            if(!orderIdMap.containsKey(orderId)){
                orderIdMap.put(orderId,"");
            }
        }

        Set<String> orderIdSet = orderIdMap.keySet();

        int count = 0;

        for (String orderId : orderIdSet){
            QueryWrapper<Order> orderQuery = new QueryWrapper<>();
            orderId = "TW"+orderId;
            orderQuery.eq("lcbh",orderId);
            count = orderService.count(orderQuery);
            if(count>0){
                return JsonResult.ok("201",orderId+"已存在!");
            }
        }
        return JsonResult.ok("200","处理成功");
    }
}
