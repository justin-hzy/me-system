package com.me.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
public class EcHttpUtil {

    private static String ebDomain = "http://nt96wea-eb.eccang.com";//eb域名  开发

    private static String wmsDomain = "http://nt96wea.eccang.com";//wms域名 开发

    private static String userName = "admin";//账号名
    private static String userPass = "eccang123456";//密码

    /**
     * Soap请求
     *
     * @param service    方法名
     * @param systemCode 所属系统代码
     * @param params     请求参数
     * @return array
     */
    public static JSONObject soapRequest(String service, String systemCode, String params) {
        JSONObject result = new JSONObject();
        systemCode = systemCode.toUpperCase();//转大写
        if (!systemCode.equals("EB") && !systemCode.equals("WMS")) {
            result.put("message", "systemCode Error");
            return result;
        }
        String domain = systemCode.equals("EB") ? ebDomain : wmsDomain;
        if (domain.isEmpty()) {
            result.put("message", "domain Empty");
            return result;
        }
        domain = domain.replaceAll("/+$", "\"");
        String wsdl = domain + "/default/svc-open/web-service-v2";
        String method = "callService";
        StringBuffer sendSoapString = getSendSoapString(params, service);
        System.out.println("sendSoapString="+sendSoapString);
        try {
            String ret = getWebServiceAndSoap(wsdl, method, service, sendSoapString);
            log.info("ret="+ret);
            Document doc = Jsoup.parse(ret);
            String json = doc.select("response").text();
//            System.out.println(json);
            return JSON.parseObject(json);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param service 方法名
     * @param params  请求参数
     * @return StringBuffer
     */
    private static StringBuffer getSendSoapString(String params, String service) {
        // TODO Auto-generated method stub
        StringBuffer sendSoapString = new StringBuffer();
        sendSoapString.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.example.org/Ec/\">");
        sendSoapString.append("   <soapenv:Body>");
        sendSoapString.append("      <ns1:callService>");
        sendSoapString.append("        <paramsJson>" + params + "</paramsJson>");
        sendSoapString.append("         <userName>" + "ZZJ" + "</userName>");
        sendSoapString.append("         <userPass>" + "Winona2023" + "</userPass>");
        sendSoapString.append("         <service>" + service + "</service>");
        sendSoapString.append("         <apiType>" + 1 + "</apiType>");
        sendSoapString.append("         <serviceTypeCode>" + "JYY" + "</serviceTypeCode>");
        sendSoapString.append("         <serviceToken>" + "JYYWMSECOPENAPI" + "</serviceToken>");
        sendSoapString.append("      </ns1:callService>");
        sendSoapString.append("   </soapenv:Body>");
        sendSoapString.append("</soapenv:Envelope>");
        return sendSoapString;
    }

    /*
     * 远程访问SOAP协议接口
     *
     * @param url： 服务接口地址
     * EB接入地址地址路径：http://xxx-EB.xxx.com/default/svc-open/web-service-v2
     * WMS接入地址地址路径：http://xxx.xxx.com/default/svc-open/web-service-v2
     * @param isClass：接口类名
     * @param isMethod： 接口方法名
     * @param sendSoapString： soap协议xml格式访问接口
     * @return  soap协议xml格式
     * @备注：有四种请求头格式1、SOAP 1.1； 2、SOAP 1.2 ； 3、HTTP GET； 4、HTTP POST
     */
    private static String getWebServiceAndSoap(String url, String isClass, String isMethod, StringBuffer sendSoapString) throws IOException {
        String soap = sendSoapString.toString();
        if (soap == null) {
            return null;
        }
        URL soapUrl = new URL(url);
        URLConnection conn = soapUrl.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Length",
                Integer.toString(soap.length()));
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        // 调用的接口方法是
        conn.setRequestProperty(isClass, isMethod);
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
        osw.write(soap);
        osw.flush();
        osw.close();
        // 获取webserivce返回的流
        InputStream is = conn.getInputStream();
//        if (is != null) {
//            byte[] bytes = new byte[0];
//            bytes = new byte[is.available()];
//            is.read(bytes);
//            String str = new String(bytes);
//            return str;
//        } else {
//            return null;
//        }
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
