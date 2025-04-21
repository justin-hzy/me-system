package com.me.modules.dms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.core.JsonResult;
import com.me.modules.dms.sys.entity.Client;
import com.me.modules.dms.sys.service.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("client")
@AllArgsConstructor
@Slf4j
//@CrossOrigin(origins = "http://120.77.244.137:8080",methods = {RequestMethod.GET,RequestMethod.POST})
//@CrossOrigin(origins = "http://39.108.136.182:8080",methods = {RequestMethod.GET,RequestMethod.POST})
@CrossOrigin
public class ClientController {

    private ClientService clientService;

    @PostMapping("checkClient")
    private JsonResult checkClient(@RequestBody String clientName){

        log.info("clientName="+clientName);
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("khmcst",clientName).eq("sfhz","0");
        Client client = clientService.getOne(queryWrapper);
        if(client == null){
            return JsonResult.ok("200","处理成功");
        }else {
            return JsonResult.ok("500","客户已存在");
        }
    }

    @GetMapping("test")
    private String test(){
        log.info("hello world");
        return "hello world";
    }
}
