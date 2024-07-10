package com.me.modules.cbsb.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

public class DemoMain1 {

    public static void main(String[] args) {
        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //restful接口url
        HttpPost httpPost = new HttpPost("http://39.108.136.182:8080/api/cube/restful/interface/saveOrUpdateModeData/ModeDataService_48");

        String currentDate = getCurrentDate();

        String currentTime = getCurrentTime();

        String currentTimeTamp = getTimestamp();

        //系统标识
        String systemid = "44E03EF5B5D5E628DC5CAB13A82251EB";
        //密码
        String d_password = "kdApi@2023";
        String md5Source = systemid+d_password+currentTimeTamp;
        String md5OfStr = getMD5Str(md5Source).toLowerCase();

        Map paramDatajson = new HashMap<>();
        Map<String,Object> params = new HashMap<>();

        //header
        Map header = new HashMap<>();
        header.put("Md5",md5OfStr);
        header.put("systemid",systemid);
        header.put("currentDateTime",currentTimeTamp);

        //封装operationinfo参数
        JSONObject operationinfo = new JSONObject();
        operationinfo.put("operator", "15273");
        operationinfo.put("operationDate",currentDate);
        operationinfo.put("operationTime",currentTime);

        //封装mainTable参数
        JSONObject mainTable = new JSONObject();
        mainTable.put("fkzh", "无");
        mainTable.put("fkrq", "2023-08-10");
        mainTable.put("skgs", "屈臣氏付款");
        mainTable.put("skyh", "工商银行测试");
        mainTable.put("je", "11");
        mainTable.put("fkfs", "电汇");
        mainTable.put("zy", "hello_world");
        mainTable.put("fkfs", "0");
        mainTable.put("zt", "0");


        /*JSONObject dataElement = new JSONObject();

        dataElement.put("mainTable",mainTable);
        dataElement.put("operationinfo",operationinfo);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(dataElement);

        JSONObject data = new JSONObject();
        data.put("data",jsonArray);

        JSONObject dataJson = new JSONObject();
        dataJson.put("datajson",data);*/



        paramDatajson.put("header",header);
        paramDatajson.put("mainTable",mainTable);
        paramDatajson.put("operationinfo",operationinfo);
        /*实体类封装方法*/

        //装填参数
        List nvps = new ArrayList();
        if(params!=null){
            for (Map.Entry entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey().toString(), JSONObject.toJSONString(entry.getValue())));
            }
        }
        try{
            httpPost.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpClient.execute(httpPost);
            if (response != null && response.getEntity() != null) {
                //返回信息
                String resulString = EntityUtils.toString(response.getEntity());

                //todo这里处理返回信息

                System.out.println("成功"+ resulString);


            }else{
                System.out.println("获取数据失败，请查看日志"+currentDate+" "+currentTime);
            }
        }catch (Exception e){
            System.out.println("请求失败"+currentDate+" "+currentTime+"====errormsg:"+e.getMessage());
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
}
