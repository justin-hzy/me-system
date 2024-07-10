package com.me.bytenew;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.bi.entity.*;
import com.me.modules.bi.pojo.ElementColumn;
import com.me.modules.bi.pojo.TableColumn;
import com.me.modules.bi.service.ByteNewEumService;
import com.me.modules.bi.service.ReissueReasonService;
import com.me.modules.bi.service.TransByteNewDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@Slf4j
public class ByteNewTest {

    @Autowired
    private TransByteNewDataService transByteNewDataService;

    @Autowired
    private ReissueReasonService reissueReasonService;

    @Autowired
    private ByteNewEumService byteNewEumService;

    @Test
    public void byteNewQueryData(){
        //获取枚举
        String app_key = "3331225291";
        String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
        //359 仓库售后异常等级表  558 过敏订单 1349 补发单
        String project_id = "359";
        String app_secret = "71dc01248f11697674f3e610971c3013";

        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "column.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        params.put("timestamp", date);
        params.put("project_id", project_id);
        params.put("sign", null);

        // 生成 sign 参数
        params.put("sign", generate_sign(params, app_secret));

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + urlencode(params);
        full_url = full_url.replace(" ","%20");


        // 发送请求并获取响应
        // 使用你喜欢的 HTTP 请求库发送 GET 请求
        // 这里只是一个示例
        String columnResponse = send_get_request(full_url);
        System.out.println("columnResponse="+columnResponse);

        JSONObject columnJsonObject = JSON.parseObject(columnResponse);
        JSONArray columnResultArray = columnJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");
        //System.out.println("columnResultArray="+columnResultArray);

        //定义数组大小
        List<TableColumn> tableColumnList = new ArrayList<>(columnResultArray.size());
        for (int i = 0; i < columnResultArray.size(); i++) {
            JSONObject columnElement = columnResultArray.getJSONObject(i);
            TableColumn tableColumn = new TableColumn();
            for (String key : columnElement.keySet()) {
                Object value = columnElement.get(key);
                // 在这里处理每个值
                if(value != null){
                    if("column_id".equals(key)){
                        tableColumn.setColumn_id(value.toString());
                    }
                    if("name".equals(key)){
                        tableColumn.setName(value.toString());
                    }
                    if("options".equals(key)){
                        JSONArray jsonArray = JSONArray.parseArray(value.toString());
                        //System.out.println(value.toString());
                        for(int j = 0; j < jsonArray.size(); j++){
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            String title = jsonObject.getString("title");
                            String id = jsonObject.getString("id");
                            ElementColumn elementColumn = new ElementColumn();
                            elementColumn.setId(id);
                            elementColumn.setTitle(title);
                            tableColumn.getOptions().add(elementColumn);
                        }
                        //System.out.println("options.size="+tableColumn.getOptions().size());
                    }

                    if("relation_options".equals(key)){
                        JSONArray jsonArray = JSONArray.parseArray(value.toString());
                        //System.out.println(value.toString());
                        for(int j = 0; j < jsonArray.size(); j++){
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            String title = jsonObject.getString("title");
                            String id = jsonObject.getString("id");
                            ElementColumn elementColumn = new ElementColumn();
                            elementColumn.setId(id);
                            elementColumn.setTitle(title);
                            tableColumn.getRelation_options().add(elementColumn);

                        }
                        //System.out.println("relation_options.size="+tableColumn.getRelation_options().size());
                    }

                    if("son_column_bos".equals(key)){
                        JSONArray jsonArray = JSONArray.parseArray(value.toString());
                        //System.out.println(value.toString());
                        for(int j = 0; j < jsonArray.size(); j++){
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            String title = jsonObject.getString("title");
                            String id = jsonObject.getString("id");
                            ElementColumn elementColumn = new ElementColumn();
                            elementColumn.setId(id);
                            elementColumn.setTitle(title);
                            tableColumn.getSon_column_bos().add(elementColumn);
                        }
                        //System.out.println("relation_options.size="+tableColumn.getSon_column_bos().size());
                    }
                }
            }
            tableColumnList.add(tableColumn);
        }
        System.out.println("tableColumnList="+tableColumnList.toString());


        //System.out.println("params="+params.toString());
        //获取工作表数据
        //task.list column.list
        params.put("method", "task.list.new");
        params.put("page_size","50");
        params.put("page_num","87");
        params.put("sign", generate_sign(params, app_secret));
        //System.out.println("params="+params.toString());
        full_url = base_url + "?" + urlencode(params);
        full_url = full_url.replace(" ","%20");

        String dataResponse = send_get_request(full_url);
        System.out.println("dataResponse="+dataResponse);



        JSONObject dataJsonObject = JSON.parseObject(dataResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");
        System.out.println("dataResultArray="+dataResultArray);


        //定义数组大小
        List<TableColumn> dataResultList = new ArrayList<>(dataResultArray.size());
        for (int i = 0; i < dataResultArray.size(); i++) {
            StoreAfSalesException storeAfSalesException = new StoreAfSalesException();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for(String key :dataElement.keySet()){
                if("1".equals(key)){
                    storeAfSalesException.setCreator("");
                }

            }

            /*System.out.println(dataElement.toString());*/

        }
    }

    @Test
    public void queryEum(){
        //获取枚举
        String app_key = "3331225291";
        String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
        //359 仓库售后异常等级表  558 过敏订单 1349 补发单
        String project_id = "1349";
        String app_secret = "71dc01248f11697674f3e610971c3013";

        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "column.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        params.put("timestamp", date);
        params.put("project_id", project_id);
        params.put("sign", null);

        // 生成 sign 参数
        params.put("sign", generate_sign(params, app_secret));

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + urlencode(params);
        full_url = full_url.replace(" ","%20");


        // 发送请求并获取响应
        // 使用你喜欢的 HTTP 请求库发送 GET 请求
        // 这里只是一个示例
        String columnResponse = send_get_request(full_url);
        System.out.println("columnResponse="+columnResponse);

        JSONObject dataJsonObject = JSON.parseObject(columnResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            String columnId = dataElement.getString("column_id");
            if("1608".equals(columnId)){
                JSONArray relationOptions = dataElement.getJSONArray("relation_options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject reasonJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<ReissueReason> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",reasonJsonObj.getString("id"));
                    ReissueReason reissueReason = reissueReasonService.getOne(queryWrapper);
                    if(reissueReason == null){
                        ReissueReason saveReason = new ReissueReason();
                        saveReason.setId(reasonJsonObj.getString("id"));
                        saveReason.setReason(reasonJsonObj.getString("title"));
                        reissueReasonService.save(saveReason);
                    }
                }
            }
//            for(String key :dataElement.keySet()){
//                if("column_id".equals(key) && "1608".equals(dataElement.get(key).toString())){
//
//                }
//            }
        }
    }

    public static String generate_sign(Map<String, String> params, String app_secret) {
        // 排除 sign 参数
        params.remove("sign");

        // 按照字母先后顺序对参数进行排序
        List<Map.Entry<String, String>> sorted_params = new ArrayList<>(params.entrySet());
        Collections.sort(sorted_params, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        // 将排序后的参数拼接为字符串
        StringBuilder sign_str = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted_params) {
            sign_str.append(entry.getKey()).append(entry.getValue());
        }

        // 将 app_secret 添加到字符串头尾
        sign_str.insert(0, app_secret);
        sign_str.append(app_secret);
        System.out.println("sign_str="+sign_str);

        // 进行 MD5 加密
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(sign_str.toString().getBytes("UTF-8"));
            StringBuilder sign = new StringBuilder();
            for (byte b : digest) {
                sign.append(String.format("%02X", b));
            }
            return sign.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String urlencode(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }
            encodedParams.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return encodedParams.toString();
    }

    public static String send_get_request(String url) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent","Apifox/1.0.0 (https://apifox.com)");
            httpGet.setHeader("Content-type","Application/json");
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String send_post_request(String url,Map<String, String> params) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type","Application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Test
    public void testStr(){
        String value = "459-151-";
        value = value.substring(0, value.length() - 1);
        System.out.println(value);
    }

    @Test
    public void queryShop(){
        String app_key = "3331225291";
        String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
        //359 仓库售后异常等级表  558 过敏订单 1349 补发单
        //String project_id = "359";
        String app_secret = "71dc01248f11697674f3e610971c3013";

        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "common.getSeller");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        params.put("timestamp", date);
        //params.put("project_id", project_id);

        // 生成 sign 参数
        params.put("sign", generate_sign(params, app_secret));

        String base_url = "https://open.bytenew.com/gateway/api/bnAPI2";
        String full_url = base_url + "?" + urlencode(params);
        full_url = full_url.replace(" ","%20");

        String responseBody = send_get_request(full_url);
        System.out.println(responseBody);
    }

    @Test
    public void queryUser(){
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", "3331225291");
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method","user.list");
        params.put("access_token", "f0507b2d2d2d3d91f741c4d4277344ba");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", generate_sign(params, "71dc01248f11697674f3e610971c3013"));
        String base_url = "https://open.bytenew.com/gateway/api/user";
        String full_url = base_url + "?" + urlencode(params);
        full_url = full_url.replace(" ","%20");
        System.out.println("full_url="+full_url);
        // 设置请求体参数
        Map<String,String> body = new HashMap<>();
        body.put("pageSize","100");
        body.put("pageNum","1");
        String responseBody = send_post_request(full_url,body);
        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONObject("map").getJSONArray("result");
        List<ByteNewUser> byteNewUserList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            ByteNewUser byteNewUser = new ByteNewUser();
            for(String key : dataElement.keySet()){
                if("nick".equals(key)){
                    byteNewUser.setNick(dataElement.get(key).toString());
                }
                if("id".equals(key)){
                    byteNewUser.setId(dataElement.get(key).toString());
                }
            }
            byteNewUserList.add(byteNewUser);
        }
        for (ByteNewUser byteNewUser : byteNewUserList){
            System.out.println(byteNewUser.getId()+","+ byteNewUser.getNick());
        }
        System.out.println(byteNewUserList.toString());
    }

    @Test
    public void queryBanUser(){

    }


    @Test
    public void getJson() throws IOException {
        String value= "645,643,650,642";
        List<String> valueList = Arrays.asList(value.split(","));
        for(String element : valueList){
            System.out.println(element);
        }
    }

    @Test
    public void queryReissueOrder(){
        log.info("初始化补发单枚举数据开始...");
        byteNewEumService.initReissueOrder();
        log.info("初始化补发单枚举数据结束...");

        log.info("补发单同步开始...");
        Map<String,Integer> map = transByteNewDataService.transReissueOrder();
        Integer totalPageNum = map.get("total_page_num");
        for(int i=2;i<=totalPageNum;i++){
            String pageNum = String.valueOf(i);
            transByteNewDataService.transReissueOrder(pageNum);
        }
        log.info("补发单同步结束...");

        log.info("补发单同步回收站同步完结...");
        Map<String,Integer> map1 = transByteNewDataService.transRecycleTransReissueOrder();
        Integer totalPageNum1 = map1.get("total_page_num");
        for(int i=2;i<=totalPageNum1;i++){
            String pageNum = String.valueOf(i);
            transByteNewDataService.transRecycleTransReissueOrder(pageNum);
        }
        log.info("补发单同步回收站同步结束...");
    }

    @Test
    public void initStoreAfSalesException(){
        log.info("初始化仓库售后异常登记表枚举数据开始...");
        byteNewEumService.initStoreAfSalesException();
        log.info("初始化仓库售后异常登记表枚举数据结束...");
    }

    @Test
    public void initShop(){
        log.info("初始化店铺数据开始...");
        byteNewEumService.initShop();
        log.info("初始化店铺数据结束...");
    }

    @Test
    public void initUser(){
        //当用户超过100就启用这个代码块
        Map<String,Integer> map = byteNewEumService.initUser();

        Integer total = map.get("total");
        //System.out.println("total="+total);
        Integer pageNum = map.get("pageNum");
        //System.out.println("pageNum="+pageNum);
        Integer pageSize = map.get("pageSize");
        System.out.println(total);
        for(;total>pageSize*pageNum;){
            pageNum = pageNum + 1;
            byteNewEumService.initUser(pageNum.toString());
        }
        //byteNewEumService.initUser("4");
    }

    @Test
    public void initAllergyReactionOrder(){
        log.info("初始化过敏订单数据开始...");
        byteNewEumService.initAllergyReactionOrder();
        log.info("初始化过敏订单数据结束...");
    }

    @Test
    void test1(){
        String aa = null;
        System.out.println(aa);
    }
}
