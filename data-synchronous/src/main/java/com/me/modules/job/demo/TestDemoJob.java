package com.me.modules.job.demo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class TestDemoJob {

    public static void main(String[] args) {

        int count = 0;
        JobDetail jobDetail = JobBuilder.newJob(DemoJob.class)
                .withIdentity("demoJob1","group1")
                .usingJobData("job","jobDetail")
                .usingJobData("name","jobDetail")
                .usingJobData("count",count)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1","trigger1")
                .usingJobData("trigger","trigger")
                //.usingJobData("count",count)
                //.usingJobData("name","trigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1)
                        .repeatForever())
                .build();

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        }catch (SchedulerException e){
            e.printStackTrace();
        }





    }
}
