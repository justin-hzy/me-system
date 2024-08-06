package com.me.nascent.modules.qimen.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class QiMenCustomerServiceImpl implements QiMenCustomerService {
    @Override
    public String transCustomer(QiMenDto dto) {

        String message = dto.getMessage();

        message = "{\"city\":\"New York\",\"age\":30,\"name\":\"John\"}";

        /*String sign = dto.getSign();*/

        Map<String, String> map = convertJsonToMap(message);


        List<Map<String, String>> collectionM = new ArrayList<>();

        collectionM.add(map);

        String result  = sortAndConcatenateParameters(collectionM);

        String secret = "";

        String stringSignTemp = secret+result;





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
