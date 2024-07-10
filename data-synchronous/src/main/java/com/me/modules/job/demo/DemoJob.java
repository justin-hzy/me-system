package com.me.modules.job.demo;

import com.me.common.aop.Mdc;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class DemoJob implements Job {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /*log.info("#######平台定时任务测试定时任务开始...#######");
        System.out.println("hello world :"+new Date());
        log.info("#######平台定时任务测试定时任务结束...#######");*/
        /*JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        JobDataMap triggerMap = jobExecutionContext.getTrigger().getJobDataMap();
        JobDataMap mergeMap = jobExecutionContext.getMergedJobDataMap();
        System.out.println("jobDetailMap:"+jobDataMap.getString("job"));
        System.out.println("triggerMap:"+triggerMap.getString("trigger"));
        System.out.println("mergeMap:"+mergeMap.getString("trigger"));
        System.out.println("---------------------");
        System.out.println("name:"+name);*/

        /*System.out.println("jobDetail:"+System.identityHashCode(jobExecutionContext.getJobDetail()));
        System.out.println("job"+System.identityHashCode(jobExecutionContext.getJobInstance()));*/

        /*System.out.println("hello world :"+new Date());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        jobDataMap.put("count",jobDataMap.getInt("count")+1);
        System.out.println("triggerMap count:"+jobDataMap.getInt("count"));






    }
}
