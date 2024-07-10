package com.me.modules.demo.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.emus.QuartzJobGroupEnum;
import com.me.common.untils.JobUtils;
import com.me.common.untils.WdtClient;
import com.me.modules.bpwms.job.quartz.BpwmsJob;
import com.me.modules.demo.dto.DemoReqDto;
import com.me.modules.demo.entity.WangDianApiGoods;
import com.me.modules.demo.service.BPayableService;
import com.me.modules.demo.service.WangDianApiGoodsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.quartz.Scheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;


/*测试接口*/
@RestController
@RequestMapping("/demo")
@Slf4j
@AllArgsConstructor
public class DemoController {


    private BPayableService bPayableService;
    private WangDianApiGoodsService wangDianApiGoodsService;
    private final JobUtils jobUtil;
    private final Scheduler scheduler;

    @GetMapping("/index")
    public String index(){
        log.info("执行index接口");
        WdtClient client = new WdtClient("apidevnew2", "wmt2-test3", "273431663", "https://sandbox.wangdian.cn/openapi2/");
        Map<String, String> params = new HashMap<String, String>();
        params.put("class_name", "月季");
        try {
            String response = client.execute("goods_class_query.php", params);
            System.out.println("response:"+response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "hello world";
    }

    @GetMapping("/oracleInsertDemo")
    public String oracleDemo(){
        /*DemoReqDto demoReqDto = new DemoReqDto();
        demoReqDto.setId("2");
        bPayableService.queryBPayable(demoReqDto);*/
        /*BPayable bPayable = new BPayable();
        *//*bPayable.setId(BigDecimal.valueOf(3));*//*
        bPayable.setAdClientId(BigDecimal.valueOf(38));
        bPayable.setAdOrgId(BigDecimal.valueOf(28));
        bPayableService.save(bPayable);*/
        WangDianApiGoods wangDianApiGoods  = new WangDianApiGoods();
        wangDianApiGoods.setPlatFormId("127");
        wangDianApiGoods.setShopNo("ls001");
        wangDianApiGoods.setEdiFlag(BigDecimal.valueOf(80));
        wangDianApiGoods.setResults("WangDianReturn(code=15, message=Remote service error, Status=false, StatusCode=null, Msg=null, sub_message=无接口调用权限，请于开放平台[应用管理]中点击[新增]申请权限, Timestamp=null, Sign=null, RequestId=15sa0stmfit9y, PageCount=null, total_count=null, Result=null, goods_list=null, stockout_list=null, stockin_list=null)");
        wangDianApiGoods.setMProductId(BigDecimal.valueOf(124));
        wangDianApiGoods.setCreateTime(new Date());
        wangDianApiGoods.setUpdateTime(new Date());
        wangDianApiGoodsService.insertWangDianApiGoodsBySql(wangDianApiGoods);
        return "success";
    }

    @GetMapping("/oracleQueryDemo")
    public String oracleQueryDemo(){
        /*DemoReqDto demoReqDto = new DemoReqDto();
        demoReqDto.setId("2");
        bPayableService.queryBPayable(demoReqDto);*/
        WangDianApiGoods wangDianApiGoods = new WangDianApiGoods();
        wangDianApiGoods.setId(BigDecimal.valueOf(5));
        wangDianApiGoodsService.queryWangDianApiGoodsById(wangDianApiGoods);
        return "success";
    }

    @GetMapping("/addQuartz")
    public String addQuartz(){

        String cron = "* * * * * ?";
        String slipBatchNo = "BpwmsJob";
        //开启定时任务
        jobUtil.createJobByCron(scheduler, slipBatchNo, QuartzJobGroupEnum.BPWMS_JOB_GROUP.getGroupName(),
                QuartzJobGroupEnum.BPWMS_JOB_GROUP.getDescription(), cron, BpwmsJob.class);

        return "success";
    }

    @GetMapping("/dmsDemo")
    public String dmsDemo(){
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建HttpGet请求对象
        HttpPost httpPost = new HttpPost("http://example.com/api/endpoint");

        try {
            // 设置参数
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("param1", "value1"));
            params.add(new BasicNameValuePair("param2", "value2"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // 发送请求，获取响应
            HttpResponse response = httpClient.execute(httpPost);

            // 获取响应状态码
            int statusCode = response.getStatusLine().getStatusCode();

            // 获取响应内容
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            // 输出响应结果
            System.out.println("Response Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭HttpClient连接
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";
    }
}
