package com.me.modules.cbsb.demo;

import com.alibaba.fastjson.JSONObject;
import com.me.common.pojo.Header;
import com.me.common.pojo.Operationinfo;
import com.me.modules.cbsb.pojo.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.math.BigInteger;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoMain2 {

    public static void main(String[] args) {
        /*String str = "44E03EF5B5D5E628DC5CAB13A82251EBbjApi@202320231103112150";
        System.out.println("20231103112150");
        System.out.println(getTimestamp());
        System.out.println(getMD5Str(str));*/

        // 创建HttpClient对象
        HttpClient httpClient = HttpClientBuilder.create().build();

        //restful接口url
        HttpPost httpPost = new HttpPost("http://39.108.136.182:8080/api/cube/restful/interface/saveOrUpdateModeData/ModeDataService_80");

        String currentDate = getCurrentDate();

        String currentTime = getCurrentTime();

        String currentTimeTamp = getTimestamp();

        //系统标识
        String systemid = "44E03EF5B5D5E628DC5CAB13A82251EB";
        //密码
        String d_password = "kdApi@2023";
        String md5Source = systemid+d_password+currentTimeTamp;
        String md5OfStr = getMD5Str(md5Source).toLowerCase();
        System.out.println(md5OfStr);


        //实体类封装方法
        //header
        Header header = new Header();
        header.setSystemid(systemid);
        header.setMd5(md5OfStr);
        header.setCurrentDateTime(currentTimeTamp);

        //封装operationinfo参数
        Operationinfo operationinfo = new Operationinfo();
        operationinfo.setOperator("298");
        operationinfo.setOperationDate(currentDate);
        operationinfo.setOperationTime(currentTime);

        //封装mainTable参数
        MainTable mainTable = new MainTable();
        mainTable.setXjlx("hello_world");
        mainTable.setFkrq("2023-08-10");
        mainTable.setKhjsfs("hello_world");
        mainTable.setSkyh("工商银行测试");
        mainTable.setSzkh("工商银行测试");
        mainTable.setSkzh("工商银行测试");
        mainTable.setSkgs("屈臣氏付款");
        mainTable.setFkdw("0");
        mainTable.setFkyh("hello_world");
        mainTable.setJe("11");
        mainTable.setFkzh("无");
        mainTable.setFkfs("电汇");
        mainTable.setZy("hello_world");

        Data data = new Data();
        data.setMainTable(mainTable);
        data.setOperationinfo(operationinfo);

        List list = new ArrayList<>();
        list.add(data);

        Datajson datajson = new Datajson();
        datajson.setData(list);
        datajson.setHeader(header);

        String dataJson = JSONObject.toJSONString(datajson);
        dataJson = dataJson.replace("md5","Md5");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("datajson",dataJson));
        System.out.println(params);
        try{
            httpPost.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);
            // 解析响应
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            System.out.println(responseBody);
            // 关闭连接
            EntityUtils.consume(responseEntity);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getCurrentTime() {
        Date newdate = new Date();
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        String currenttime = (timestamp.toString()).substring(11, 13) + ":" + (timestamp.toString()).substring(14, 16) + ":"
                + (timestamp.toString()).substring(17, 19);
        return currenttime;
    }

    public static String getCurrentDate() {
        Date newdate = new Date();
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        String currentdate = (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-"
                + (timestamp.toString()).substring(8, 10);
        return currentdate;
    }

    /**
     * 获取当前日期时间。 YYYY-MM-DD HH:MM:SS
     * @return		当前日期时间
     */
    public static String getCurDateTime() {
        Date newdate = new Date();
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        return (timestamp.toString()).substring(0, 19);
    }

    /**
     * 获取时间戳   格式如：19990101235959
     * @return
     */
    public static String getTimestamp(){
        return getCurDateTime().replace("-", "").replace(":", "").replace(" ", "");
    }

    public static int getIntValue(String v, int def) {
        try {
            return Integer.parseInt(v);
        } catch (Exception ex) {
            return def;
        }
    }


    public static String null2String(Object s) {
        return s == null ? "" : s.toString();

    }

    public static String getMD5Str(String plainText){
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //throw new RuntimeException("没有md5这个算法！");
            //throw new RuntimeException(SystemEnv.getHtmlLabelName(517545,userLanguage));
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);
        // 如果生成数字未满32位，需要前面补0
        // 不能把变量放到循环条件，值改变之后会导致条件变化。如果生成30位 只能生成31位md5
        int tempIndex = 32 - md5code.length();
        for (int i = 0; i < tempIndex; i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /*public static String getMD5Str(String plainText) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
