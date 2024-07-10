package com.me.modules.bos.other.controller;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.bos.other.dto.PostOtherInDto;
import com.me.modules.bos.other.dto.PostOtherOutDto;
import com.me.modules.bos.other.entity.BosOtherLog;
import com.me.modules.bos.other.service.BosOtherLogService;
import com.me.modules.bos.other.service.OtherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("bos")
@AllArgsConstructor
@Slf4j
public class OtherController {

    private OtherService otherService;

    private BosOtherLogService bosOtherLogService;


    @PostMapping("PostOtherIn")
    public JSONObject PostOther(@RequestBody PostOtherInDto reqDto){
        log.info("reqDto="+reqDto);
        log.info("执行 PostOther----------------requestId="+reqDto.getRequestId());
        JSONObject retJson = otherService.PostOtherIn(reqDto);

        String code = retJson.getString("code");
        BosOtherLog bosOtherLog = new BosOtherLog();
        if("500".equals(code)){
            bosOtherLog.setRequestId(reqDto.getRequestId());
            JSONObject param = new JSONObject();
            param.put("sku",reqDto.getSku());
            param.put("qty",reqDto.getQty());
            param.put("description",reqDto.getDescription());
            param.put("billDate",reqDto.getBillDate());
            param.put("cstore",reqDto.getCstore());

            bosOtherLog.setParam(param.toJSONString());

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayString = today.format(formatter);
            bosOtherLog.setCreateTime(todayString);
            bosOtherLog.setStatus("0");
            bosOtherLog.setIs_delete("0");
        }else if("200".equals(code)){
            bosOtherLog.setRequestId(reqDto.getRequestId());
            JSONObject param = new JSONObject();
            param.put("sku",reqDto.getSku());
            param.put("qty",reqDto.getQty());
            param.put("description",reqDto.getDescription());
            param.put("billDate",reqDto.getBillDate());
            param.put("cstore",reqDto.getCstore());

            bosOtherLog.setParam(param.toJSONString());

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayString = today.format(formatter);
            bosOtherLog.setCreateTime(todayString);
            bosOtherLog.setStatus("1");
            bosOtherLog.setIs_delete("0");
        }
        bosOtherLogService.save(bosOtherLog);
        return retJson;
    }

    @PostMapping("postReOther")
    public JSONObject postReOther(@RequestBody PostOtherOutDto reqDto){
        log.info("reqDto="+reqDto);
        log.info("执行 postReOther----------------requestId="+reqDto.getRequestId());
        JSONObject retJson = otherService.postReOther(reqDto);

        String code = retJson.getString("code");
        BosOtherLog bosOtherLog = new BosOtherLog();
        if("500".equals(code)){
            bosOtherLog.setRequestId(reqDto.getRequestId());
            JSONObject param = new JSONObject();
            param.put("sku",reqDto.getSku());
            param.put("qty",reqDto.getQty());
            param.put("description",reqDto.getDescription());
            param.put("billDate",reqDto.getBillDate());
            param.put("cstore",reqDto.getCstore());

            bosOtherLog.setParam(param.toJSONString());

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayString = today.format(formatter);
            bosOtherLog.setCreateTime(todayString);
            bosOtherLog.setStatus("0");
            bosOtherLog.setIs_delete("0");
        }else if("200".equals(code)){
            bosOtherLog.setRequestId(reqDto.getRequestId());
            JSONObject param = new JSONObject();
            param.put("sku",reqDto.getSku());
            param.put("qty",reqDto.getQty());
            param.put("description",reqDto.getDescription());
            param.put("billDate",reqDto.getBillDate());
            param.put("cstore",reqDto.getCstore());

            bosOtherLog.setParam(param.toJSONString());

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayString = today.format(formatter);
            bosOtherLog.setCreateTime(todayString);
            bosOtherLog.setStatus("1");
            bosOtherLog.setIs_delete("0");
        }
        bosOtherLogService.save(bosOtherLog);
        return retJson;
    }

//    @PostMapping("postSetFr")
//    public JSONObject postSetFr(@RequestBody PostOtherInDto reqDto){
//        log.info("reqDto="+reqDto);
//        log.info("执行 postSetFr----------------requestId="+reqDto.getRequestId());
//        JSONObject retJson = otherService.postSetFr(reqDto);
//
//        String code = retJson.getString("code");
//        BosOtherLog bosOtherLog = new BosOtherLog();
//        if("500".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("0");
//            bosOtherLog.setIs_delete("0");
//        }else if("200".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("1");
//            bosOtherLog.setIs_delete("0");
//        }
//        bosOtherLogService.save(bosOtherLog);
//        return retJson;
//    }
//
//    @PostMapping("postSetSon")
//    public JSONObject postSetSon(@RequestBody PostOtherOutDto reqDto){
//        log.info("reqDto="+reqDto);
//        log.info("执行 postSetSon----------------requestId="+reqDto.getRequestId());
//        JSONObject retJson = otherService.postSetSon(reqDto);
//
//        String code = retJson.getString("code");
//        BosOtherLog bosOtherLog = new BosOtherLog();
//        if("500".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("0");
//            bosOtherLog.setIs_delete("0");
//        }else if("200".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("1");
//            bosOtherLog.setIs_delete("0");
//        }
//        bosOtherLogService.save(bosOtherLog);
//        return retJson;
//    }
//
//    @PostMapping("postDismantleSon")
//    public JSONObject postDismantleSon(@RequestBody PostOtherInDto reqDto){
//        log.info("reqDto="+reqDto);
//        log.info("执行 postDismantleSon----------------requestId="+reqDto.getRequestId());
//        JSONObject retJson = otherService.postDismantleSon(reqDto);
//
//        String code = retJson.getString("code");
//        BosOtherLog bosOtherLog = new BosOtherLog();
//        if("500".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("0");
//            bosOtherLog.setIs_delete("0");
//        }else if("200".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("1");
//            bosOtherLog.setIs_delete("0");
//        }
//        bosOtherLogService.save(bosOtherLog);
//        return retJson;
//    }
//
//    @PostMapping("postDismantleFr")
//    public JSONObject postDismantleFr(@RequestBody PostOtherOutDto reqDto){
//        log.info("reqDto="+reqDto);
//        log.info("执行 postDismantleFr----------------requestId="+reqDto.getRequestId());
//        JSONObject retJson = otherService.postDismantleFr(reqDto);
//
//        String code = retJson.getString("code");
//        BosOtherLog bosOtherLog = new BosOtherLog();
//        if("500".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("0");
//            bosOtherLog.setIs_delete("0");
//        }else if("200".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("1");
//            bosOtherLog.setIs_delete("0");
//        }
//        bosOtherLogService.save(bosOtherLog);
//        return retJson;
//    }
//
//    @PostMapping("postTransCodeFr")
//    public JSONObject postTransCodeFr(@RequestBody PostOtherOutDto reqDto){
//        log.info("reqDto="+reqDto);
//        log.info("执行 postTransCodeFr----------------requestId="+reqDto.getRequestId());
//        JSONObject retJson = otherService.postTransCodeFr(reqDto);
//
//        String code = retJson.getString("code");
//        BosOtherLog bosOtherLog = new BosOtherLog();
//        if("500".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("0");
//            bosOtherLog.setIs_delete("0");
//        }else if("200".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("1");
//            bosOtherLog.setIs_delete("0");
//        }
//        bosOtherLogService.save(bosOtherLog);
//        return retJson;
//    }
//
//    @PostMapping("postTransCodeSon")
//    public JSONObject postTransCodeSon(@RequestBody PostOtherInDto reqDto){
//        log.info("reqDto="+reqDto);
//        log.info("执行 postTransCodeSon----------------requestId="+reqDto.getRequestId());
//        JSONObject retJson = otherService.postTransCodeSon(reqDto);
//
//        String code = retJson.getString("code");
//        BosOtherLog bosOtherLog = new BosOtherLog();
//        if("500".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("0");
//            bosOtherLog.setIs_delete("0");
//        }else if("200".equals(code)){
//            bosOtherLog.setRequestId(reqDto.getRequestId());
//            JSONObject param = new JSONObject();
//            param.put("sku",reqDto.getSku());
//            param.put("qty",reqDto.getQty());
//            param.put("description",reqDto.getDescription());
//            param.put("billDate",reqDto.getBillDate());
//            param.put("cstore",reqDto.getCstore());
//
//            bosOtherLog.setParam(param.toJSONString());
//
//            LocalDate today = LocalDate.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String todayString = today.format(formatter);
//            bosOtherLog.setCreateTime(todayString);
//            bosOtherLog.setStatus("1");
//            bosOtherLog.setIs_delete("0");
//        }
//        bosOtherLogService.save(bosOtherLog);
//        return retJson;
//    }

    @PostMapping("getTest1")
    public String getTest1(){
        String aa = "hello world";
        log.info("aa="+aa);
        return aa;
    }
}
