package com.me.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.me.common.utils.SearchHttpAK;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
public class BaiduMapDemo {

    @Test
    public  void demo() throws Exception {
        SearchHttpAK snCal = new SearchHttpAK();
        Map params = new LinkedHashMap<String, String>();
        /*
         * 湖南省长沙市开福区高岭汽配城B区6街2222
         * 山西省太原市万柏林区兴华街道办后北屯社区中奥小区1号楼1单元101
         * 山东省济南市历城区华山街道盖世物流园五库区7-005
         * 河南省郑州市中牟县新安路普洛斯物流园A-4库屈臣氏仓库
         * 新竹縣湖口鄉中正路三段112號
         * 台北市大安區忠孝東路三段305號4樓之4
         * */
        params.put("address", "台中市龍井區新興路17號");
        params.put("ak", snCal.AK);
        params.put("model","1");
        params.put("confidence","100");
        String str = snCal.requestGetAK(snCal.URL, params);
        JSONObject jsonObject = JSON.parseObject(str);

        JSONObject resultJsonObj = jsonObject.getJSONObject("result");
        String addressDetail  = jsonObject.getJSONObject("detail").getString("address_detail");

        String city="";
        String county="";
        String town="";
        String address="";
        for (String key : resultJsonObj.keySet()){
            if(key.equals("city")){
                city = resultJsonObj.get(key).toString();
            }
            if(key.equals("county")){
                county = resultJsonObj.get(key).toString();
            }
            if(key.equals("town")){
                town = resultJsonObj.get(key).toString();
            }
        }
        System.out.println("city="+city);
        System.out.println("county="+county);
        System.out.println("town="+town);
        System.out.println(str);
        System.out.println("addressDetail="+addressDetail);
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
}
