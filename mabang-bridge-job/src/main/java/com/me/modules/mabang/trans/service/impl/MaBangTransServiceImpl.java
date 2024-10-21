package com.me.modules.mabang.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.common.config.MaBangConfig;
import com.me.modules.mabang.service.MaBangHttpService;
import com.me.modules.mabang.trans.service.MaBangTransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class MaBangTransServiceImpl implements MaBangTransService {


    private MaBangHttpService maBangHttpService;

    private MaBangConfig maBangConfig;


    @Override
    public JSONObject transMaBangOrder(String createDateStart,String createDateEnd,String cursor) {
        JSONObject returnJSON = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("createDateStart",createDateStart);
        map.put("createDateEnd",createDateEnd);
        map.put("maxRows","1");
        map.put("status","3");
        if(cursor != null){
            map.put("cursor",cursor);
        }
        String data = maBangHttpService.callGwApi("order-get-order-list-new", map);

        JSONObject respJson = JSONObject.parseObject(data);

        String code = respJson.getString("code");
        if("200".equals(code)){
            log.info("data="+data);
            JSONObject dataJson = respJson.getJSONObject("data");
            String hasNext = dataJson.getString("hasNext");
            log.info("total="+dataJson.getString("total"));
            log.info("hasNext="+hasNext);
            if("false".equals(hasNext)){
                String nextCursor = dataJson.getString("nextCursor");
                log.info("nextCursor="+nextCursor);
                returnJSON.put("nextCursor",nextCursor);
            }else {
                String nextCursor = dataJson.getString("nextCursor");
                log.info("nextCursor="+nextCursor);
                returnJSON.put("nextCursor",nextCursor);
                returnJSON.put("hasNext",hasNext);
            }
        }else {

        }

        return returnJSON;
    }

    @Override
    public JSONObject transMaBangReOrder(String createDateStart, String createDateEnd, String cursor) {

        JSONObject returnJSON = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("createDateStart",createDateStart);
        map.put("createDateEnd",createDateEnd);
        map.put("rowsPerPage","1000");
        map.put("status","4");
        if(cursor != null){
            map.put("cursor",cursor);
        }

        String data = maBangHttpService.callGwApi("order-get-return-order-list", map);

        log.info("data="+data);

        /*JSONObject respJson = JSONObject.parseObject(data);

        String code = respJson.getString("code");
        if("200".equals(code)){

        }else {

        }*/


        return returnJSON;
    }
}
