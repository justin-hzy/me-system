package com.me.demo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class FlashDemo {

    private static final String merchantID = "C2405011";
    private static final String key = "2464bb12440d0eb67a6a8a6375e0efdee7c42515c7dccbd9286ae9737206009c";

    private static final String stockQueryUrl = "https://open-training.flashfulfillment.co.th/open/outBoundOrderList";

    @Test
    public void test() throws IOException {
        FlashDemo flashDemo = new FlashDemo();
        //Map<String, String> stockQueryParam = flashDemo.getStockQueryParam();
        Map<String,String> putStockParam = flashDemo.putStockParam();
        putStockParam = flashDemo.sortedMap(putStockParam);
        String sign = GenerateAndCheckSign4Fulfilment.generateSign(putStockParam, key);
        /*putStockParam.put("sign", sign);
        flashDemo.postRequest(stockQueryUrl, putStockParam);*/
    }

    private void postRequest(String urlStr, Map<String, String> param) throws IOException {
        String urlPath = new String(urlStr);
        //create connection
        URL url = new URL(urlPath);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        //set connection properties
        httpConn.setDoOutput(true);   //output
        httpConn.setDoInput(true);   //input
        httpConn.setUseCaches(false);  //non caches
        httpConn.setRequestMethod("POST");
        //set request properties
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Charset", "UTF-8");
        //connect.No using method connect() is ok, the method below httpConn.getOutputStream() will do connection too.
        httpConn.connect();

        DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
        dos.writeBytes(convertStringParameter(param));
        dos.flush();
        dos.close();

        int resultCode = httpConn.getResponseCode();
        if (HttpURLConnection.HTTP_OK == resultCode) {
            StringBuffer sb = new StringBuffer();
            String readLine = new String();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
            System.out.println(sb.toString());
        }
    }

    public static String convertStringParameter(Map parameterMap) throws UnsupportedEncodingException {
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(URLEncoder.encode(value, "utf-8"));
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        return parameterBuffer.toString();
    }

    private Map<String, String> getStockQueryParam() {
        Map<String, String> param = new HashMap<>();
        param.put("barCode", "FEBAG01,FEBAG02,FEBAG03");
        param.put("mchId", merchantID);
        param.put("nonceStr", "yyv6YJP436wCkdpNdghC");
        param.put("warehouseId", "1");
        return param;
    }

    private Map<String, String> putStockParam(){
        Map<String, String> param = new HashMap<>();
        //JSONObject param = new JSONObject();
        param.put("status","1");
        param.put("warehouseId","1");
        param.put("type","1");
        param.put("channelSource","");
        param.put("nodeSn","TH01090201");
        param.put("consigneeName","Jack");
        param.put("consigneePhone","18615431841");
        param.put("province","bj");
        param.put("city","bj");
        param.put("district","hd");
        param.put("postalCode","100510");
        param.put("consigneeAddress","jyy");
        param.put("deliveryWay","logistics");
        param.put("outTime","2000-01-01");
        param.put("goodsStatus","normal");
        param.put("remark","normal");

        JSONArray goodList = new JSONArray();
        JSONObject goodMap = new JSONObject();
        goodMap.put("i","0");
        goodMap.put("barCode","0");
        goodMap.put("goodsName","0");
        goodMap.put("specification","0");
        goodMap.put("num","1");
        goodMap.put("price","400");
        goodMap.put("remark","remark1");
        goodList.add(goodMap);

        param.put("goods",goodList.toJSONString());

        return param;
    }

    private Map sortedMap(Map<String, String> map) {
        LinkedHashMap sortedMap = map.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldVal, newVal) -> oldVal,
                LinkedHashMap::new));
        return sortedMap;
    }
}

@Slf4j
class GenerateAndCheckSign4Fulfilment {
    public static String generateSign(Map<String, String> paramMap, String secretKey) {
        Map<String, String> acsMap = sortMapByKey(paramMap);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : acsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StrUtil.isNotEmpty(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        sb.append("key=").append(secretKey);
        log.info("sb="+sb);
        return DigestUtils4Fulfilment.sha256HexDigest(sb.toString()).toUpperCase();
    }

    public boolean checkSign(Map<String, String> dataMap, String secretKey) {
        Map<String, String> tempMap = new TreeMap<>(dataMap);
        if (tempMap.containsKey("sign")) {
            String sign = tempMap.remove("sign");
            String correctSign = this.generateSign(tempMap, secretKey);
            return correctSign.equals(sign);
        }
        return false;
    }

    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

}

class DigestUtils4Fulfilment {
    private static final String SHA256_ALGORITHM_NAME = "SHA-256";
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String sha256HexDigest(String source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA256_ALGORITHM_NAME);
            messageDigest.update(source.getBytes("UTF-8"));
            return new String(encodeHex(messageDigest.digest()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static char[] encodeHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }
}
