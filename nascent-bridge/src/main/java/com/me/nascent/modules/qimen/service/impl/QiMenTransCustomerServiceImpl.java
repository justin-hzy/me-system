package com.me.nascent.modules.qimen.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.service.QiMenTransCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class QiMenTransCustomerServiceImpl implements QiMenTransCustomerService {
    @Override
    public String transCustomer(QiMenDto dto) {

        String message = dto.getMessage();

        message = "{\"city\":\"New York\",\"age\":30,\"name\":\"John\"}";

        /*String sign = dto.getSign();*/

        Map<String, String> messageMap = convertJsonToMap(message);


        List<Map<String, String>> collectionM = new ArrayList<>();

        collectionM.add(messageMap);

        String result  = sortAndConcatenateParameters(collectionM);

        String secret = "";

        String stringSignTemp = secret+result;

        //获取会员属性
        //最新绑卡时间
        String firstBindCardTime = messageMap.get("firstBindCardTime");
        //最新解绑时间
        String lastedUnbindCardTime = messageMap.get("lastedUnbindCardTime");
        //会员级别
        String level = messageMap.get("level");
        //加密手机号
        String mixMobile = messageMap.get("mixMobile");
        //公司级会员唯一标识
        String omid = messageMap.get("omid");
        //店铺级会员唯一标识
        String ouid = messageMap.get("ouid");
        //会员积分
        String point = messageMap.get("point");
        //卖家昵称
        String sellerNick = messageMap.get("sellerNick");
        //南讯ouid
        String nasOuid = messageMap.get("nasOuid");








        return null;
    }



    public static Map<String, String> convertJsonToMap(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            Map<String, String> map = mapper.readValue(jsonString, typeFactory.constructMapType(Map.class, String.class, String.class));
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String sortAndConcatenateParameters(List<Map<String, String>> collectionM) {
        Map<String, String> params = new TreeMap<>();
        for (Map<String, String> map : collectionM) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null && !value.isEmpty()) {
                    params.put(key, value);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(entry.getKey()).append(entry.getValue());
        }

        return result.toString();
    }
}
