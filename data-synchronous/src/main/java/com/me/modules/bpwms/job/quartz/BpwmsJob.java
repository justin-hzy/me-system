package com.me.modules.bpwms.job.quartz;

import com.me.common.untils.WdtClient;
import com.me.modules.demo.service.BPayableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
@DisallowConcurrentExecution
public class BpwmsJob implements Job {

    private final BPayableService bPayableService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(Thread.currentThread().getName()+"BpwmsJob定时任务开始！start:"+new Date());
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
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"BpwmsJob定时任务结束！end:"+new Date());
    }
}
