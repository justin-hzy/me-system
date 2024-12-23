package com.me.modules.mabang.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.common.config.DmsConfig;
import com.me.modules.mabang.order.dto.InsertMBOrderDtl;
import com.me.modules.mabang.order.dto.InsertMBOrderDto;
import com.me.modules.mabang.order.entity.MBOrder;
import com.me.modules.mabang.order.mapper.MBOrderMapper;
import com.me.modules.mabang.order.service.MBOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class MBOrderServiceImpl extends ServiceImpl<MBOrderMapper, MBOrder> implements MBOrderService {

    private DmsConfig dmsConfig;

    @Override
    public void insertMBOrder(InsertMBOrderDto dto) {
        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //restful接口url
        HttpPost httpPost = new HttpPost(dmsConfig.getOrderUrl());
        //当前日期
        String currentDate = getCurrentDate();
        //当前时间
        String currentTime = getCurrentTime();
        //获取时间戳
        String currentTimeTamp = getTimestamp();

        //Map<String,Object> params = new HashMap<>();
        //Map<String,Object> paramDatajson = new HashMap<>();
        JSONObject params = new JSONObject();
        JSONObject paramDatajson = new JSONObject();



        //header
        Map header = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        //系统标识
        String systemid = "5e06b54a550bf47d0f580027e2adbf81";
        //密码
        String d_password = "mabangApi@2024";
        //封装header里的参数
        header.put("systemid",systemid);
        header.put("currentDateTime",currentTimeTamp);
        String md5Source = systemid+d_password+currentTimeTamp;
        String md5OfStr = getMD5Str(md5Source).toLowerCase();
        //Md5是：系统标识+密码+时间戳 并且md5加密的结果
        header.put("Md5",md5OfStr);
        jsonObject.put("header",header);

        //封装mainTable参数
        JSONObject mainTable = new JSONObject();
        mainTable.put("id","159");
        mainTable.put("platform_order_id", dto.getPlatformOrderId());
        mainTable.put("express_time", dto.getExpressTime());
        mainTable.put("street", dto.getStreet());
        mainTable.put("shop_id", dto.getShopId());
        mainTable.put("shop_name",dto.getShopName());
        mainTable.put("voucher_price_origin",dto.getVoucherPriceOrigin());
        mainTable.put("subsidy_amount_origin",dto.getSubsidyAmountOrigin());
        mainTable.put("shipping_total_origin",dto.getShippingTotalOrigin());
        mainTable.put("other_income",dto.getOtherIncome());
        mainTable.put("item_total_origin",dto.getItemTotalOrigin());
        String currencyId = dto.getCurrencyId();
        if ("VND".equals(currencyId)){
            currencyId = "PRE009";
        }else if("THB".equals(currencyId)){
            currencyId = "PRE012";
        }
        mainTable.put("currency_id",currencyId);
        String orderStatus = dto.getOrderStatus();
        //状态数值-1 = dms状态值(dms状态值从0开始，马帮是从1开始)
        mainTable.put("order_status",Integer.valueOf(orderStatus)-1);
        mainTable.put("is_trans_k3", "1");
        paramDatajson.put("mainTable",mainTable);

        //封装detail1 参数
        JSONArray detail1Array = new JSONArray();

        JSONObject operateJson = new JSONObject();
        operateJson.put("action","Save");
        //operateJson.put("actionDescribe","Save");

        List<InsertMBOrderDtl> dtls = dto.getInsertMBOrderDtls();
        for (InsertMBOrderDtl dtl : dtls){
            JSONObject dataJson = new JSONObject();
            dataJson.put("stock_sku",dtl.getStockSku());
            dataJson.put("quantity",dtl.getQuantity());
            dataJson.put("sell_price_origin",dtl.getSellPriceOrigin());


            JSONObject detailJson = new JSONObject();

            detailJson.put("operate",operateJson);
            detailJson.put("data",dataJson);

            detail1Array.add(detailJson);
        }


        paramDatajson.put("detail1",detail1Array);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(paramDatajson);


        jsonObject.put("data",jsonArray);

        //封装operationinfo参数
        JSONObject operationinfo = new JSONObject();
        operationinfo.put("operator", "276");
        operationinfo.put("operationDate","2024-11-21");
        operationinfo.put("operationTime","00:00:00");
        paramDatajson.put("operationinfo",operationinfo);


        params.put("datajson",jsonObject);

        /*log.info(JSONObject.toJSONString(paramDatajson.toString()));
        log.info(header.toString());*/
        log.info(params.toJSONString());
        //装填参数
        List nvps = new ArrayList();
        if(params!=null){
            for (Map.Entry entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair((String) entry.getKey(), JSONObject.toJSONString(entry.getValue())));
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

    public String getMD5Str(String plainText){
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
            //throw new RuntimeException("没有md5这个算法！");
            throw new RuntimeException(e.getMessage());
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
}
