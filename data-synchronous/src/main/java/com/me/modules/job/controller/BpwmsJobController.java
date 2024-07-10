package com.me.modules.job.controller;

import com.me.common.emus.QuartzJobGroupEnum;
import com.me.common.untils.JobUtils;
import com.me.modules.bpwms.job.quartz.BpwmsJob;
import lombok.AllArgsConstructor;
import org.quartz.Scheduler;

@AllArgsConstructor
public class BpwmsJobController {
    private final JobUtils jobUtil;
    private final Scheduler scheduler;

    public static void main(String[] args) {

    }

    public void run(){
        String cron = "0 20 16 * * ?";
        String slipBatchNo = "BpwmsQuartz";

        //开启定时任务
        jobUtil.createJobByCron(scheduler, slipBatchNo, QuartzJobGroupEnum.BPWMS_JOB_GROUP.getGroupName(),
                QuartzJobGroupEnum.BPWMS_JOB_GROUP.getDescription(), cron, BpwmsJob.class);
    }
}
