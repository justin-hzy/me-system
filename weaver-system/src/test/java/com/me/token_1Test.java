package com.me;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
@Slf4j
public class token_1Test {

    /**
     * 模拟缓存服务
     */
    private static final Map<String,String> SYSTEM_CACHE = new HashMap<>();


    /**
     * ecology系统发放的授权许可证(appid)
     */
    private static final String APPID = "0ddf6d56a167eaf78d6c6f4a8ffaa086";


    @Test
    public void tokenDemo(){

        //testRegist("http://39.108.136.182:8080/");
        //testGetoken("http://39.108.136.182:8080/");
        JSONObject jsonObject = new JSONObject();

        JSONArray mainDataArr = new JSONArray();
        JSONArray detailDataArr = new JSONArray();

        JSONObject mainData1 = new JSONObject();
        mainData1.put("fieldName","wldh");
        mainData1.put("fieldValue","测试数据");

        JSONObject mainData2 = new JSONObject();
        mainData2.put("fieldName","wlgs");
        mainData2.put("fieldValue","测试数据");

        JSONObject mainData3 = new JSONObject();
        mainData3.put("fieldName","xsckdh");
        mainData3.put("fieldValue","测试数据");

        mainDataArr.add(mainData1);
        mainDataArr.add(mainData2);
        mainDataArr.add(mainData3);

        JSONObject detailData = new JSONObject();
        detailData.put("tableDBName","formtable_main_151_dt2");

        JSONArray workflowRequestTableRecordsArr = new JSONArray();
        JSONObject workflowRequestTableRecords = new JSONObject();

        workflowRequestTableRecords.put("recordOrder",0);

        JSONArray workflowRequestTableFieldsArr = new JSONArray();


        JSONObject workflowRequestTableFields1 = new JSONObject();
        workflowRequestTableFields1.put("fieldName","xsckdh");
        workflowRequestTableFields1.put("fieldValue","1234");

        JSONObject workflowRequestTableFields2 = new JSONObject();
        workflowRequestTableFields2.put("fieldName","chrq");
        workflowRequestTableFields2.put("fieldValue","2024-01-01");

        JSONObject workflowRequestTableFields3 = new JSONObject();
        workflowRequestTableFields3.put("fieldName","hpbh");
        workflowRequestTableFields3.put("fieldValue","1234");

        JSONObject workflowRequestTableFields4 = new JSONObject();
        workflowRequestTableFields4.put("fieldName","cksl");
        workflowRequestTableFields4.put("fieldValue",4);


        workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
        workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
        workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
        workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

        workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
//        workflowRequestTableRecords.put("deleteAll","");
//        workflowRequestTableRecords.put("deleteKeys","");

        workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
        detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);

        detailDataArr.add(detailData);




        jsonObject.put("mainData",mainDataArr);
        jsonObject.put("detailData",detailDataArr);
        jsonObject.put("requestId",136142);
        log.info(jsonObject.toJSONString());





        testRestful("http://39.108.136.182:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());

    }

    /**
     * 第一步：
     *
     * 调用ecology注册接口,根据appid进行注册,将返回服务端公钥和Secret信息
     */
    @Test
    public static Map<String,Object> testRegist(String address){
        //获取当前系统RSA加密的公钥
        RSA rsa = new RSA();
        String publicKey = rsa.getPublicKeyBase64();
        String privateKey = rsa.getPrivateKeyBase64();
        // 客户端RSA私钥
        SYSTEM_CACHE.put("LOCAL_PRIVATE_KEY",privateKey);
        // 客户端RSA公钥
        SYSTEM_CACHE.put("LOCAL_PUBLIC_KEY",publicKey);
        //调用ECOLOGY系统接口进行注册
        String data = HttpRequest.post(address + "/api/ec/dev/auth/regist")
                .header("appid",APPID)
                .header("cpk",publicKey)
                .timeout(2000)
                .execute().body();
        // 打印ECOLOGY响应信息
        System.out.println("testRegist()："+data);
        Map<String,Object> datas = JSONUtil.parseObj(data);
        //ECOLOGY返回的系统公钥
        SYSTEM_CACHE.put("SERVER_PUBLIC_KEY", StrUtil.nullToEmpty((String)datas.get("spk")));
        //ECOLOGY返回的系统密钥
        SYSTEM_CACHE.put("SERVER_SECRET",StrUtil.nullToEmpty((String)datas.get("secrit")));
        return datas;
    }

    /**
     * 第二步：
     *
     * 通过第一步中注册系统返回信息进行获取token信息
     */
    public static Map<String,Object> testGetoken(String address){
        // 从系统缓存或者数据库中获取ECOLOGY系统公钥和Secret信息
        String secret = SYSTEM_CACHE.get("SERVER_SECRET");
        String spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
        // 如果为空,说明还未进行注册,调用注册接口进行注册认证与数据更新
        if (Objects.isNull(secret)||Objects.isNull(spk)){
            testRegist(address);
        // 重新获取最新ECOLOGY系统公钥和Secret信息
            secret = SYSTEM_CACHE.get("SERVER_SECRET");
            spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
        }
        // 公钥加密,所以RSA对象私钥为null
        RSA rsa = new RSA(null,spk);
        //对秘钥进行加密传输，防止篡改数据
        String encryptSecret =
                rsa.encryptBase64(secret, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
        //调用ECOLOGY系统接口进行注册
        String data = HttpRequest.post(address+ "/api/ec/dev/auth/applytoken")
                .header("appid",APPID)
                .header("secret",encryptSecret)
                .header("time","3600")
                .execute().body();
        System.out.println("testGetoken()："+data);
        Map<String,Object> datas = JSONUtil.parseObj(data);
        //ECOLOGY返回的token
        // TODO 为Token缓存设置过期时间
        SYSTEM_CACHE.put("SERVER_TOKEN",StrUtil.nullToEmpty((String)datas.get("token"))
        );
        return datas;
    }

    public static String testRestful(String address,String api,String jsonParams){
        //ECOLOGY返回的token
        String token= SYSTEM_CACHE.get("SERVER_TOKEN");
        if (StrUtil.isEmpty(token)){
            token = (String) testGetoken(address).get("token");
        }
        String spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
        //封装请求头参数
        RSA rsa = new RSA(null,spk);
        //对用户信息进行加密传输,暂仅支持传输OA用户ID
        String encryptUserid =
                rsa.encryptBase64("276",CharsetUtil.CHARSET_UTF_8,KeyType.PublicKey);
        //调用ECOLOGY系统接口
        String data = HttpRequest.get(address + api)
                .header("appid",APPID)
                .header("token",token)
                .header("userid",encryptUserid)
                //.header("skipsession","1")
                .body(jsonParams)
                .execute().body();
        System.out.println("testRestful()："+data);
        return data;
    }

    @Test
    public void test(){
        String str = "TWPOTEST219";
        str = str.replace("TW","");
        log.info(str);
    }
}
