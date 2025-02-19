package com.me.modules.lock.job;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.aop.Mdc;
import com.me.common.config.DmsConfig;
import com.me.common.utils.DmsUtil;
import com.me.modules.lock.entity.DmsLockSwitch;
import com.me.modules.lock.entity.DmsTwLock;
import com.me.modules.lock.service.DmsLockSwitchService;
import com.me.modules.lock.service.DmsTwLockService;
import com.me.modules.queue.entity.DmsTwQueue;
import com.me.modules.queue.service.DmsTwQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@Slf4j
@EnableScheduling
@AllArgsConstructor
public class K3TwLockJob {

    private DmsTwLockService dmsTwLockService;

    private DmsTwQueueService dmsTwQueueService;

    private DmsConfig dmsConfig;

    private DmsLockSwitchService dmsLockSwitchService;

    @Mdc
    @Scheduled(cron = "0 * 9-21 * * *")
    void getTwLock(){
        log.info("执行getTwLock(台湾)定时任务,查看定时任务是否可以用");
        QueryWrapper<DmsLockSwitch> dmsLockSwitchQuery = new QueryWrapper<DmsLockSwitch>();

        dmsLockSwitchQuery.eq("switch_type","tw");

        DmsLockSwitch dmsLockSwitch = dmsLockSwitchService.getOne(dmsLockSwitchQuery);

        String isOpen = dmsLockSwitch.getIsOpen();
        if ("1".equals(isOpen)){
            log.info("开关已打开，执行提交出程序");
            QueryWrapper<DmsTwLock> dmsTwLockQuery = new QueryWrapper<>();
            // lockType:0 台湾 isLock:1 已占用锁
            dmsTwLockQuery.lambda().eq(DmsTwLock::getLockType,"0").eq(DmsTwLock::getIsLock,"1") ;
            List<DmsTwLock> dmsTwLocks =  dmsTwLockService.list(dmsTwLockQuery);
            if(CollUtil.isEmpty(dmsTwLocks)){
                QueryWrapper<DmsTwQueue> dmsTwQueueQuery = new QueryWrapper<>();
                dmsTwQueueQuery.lambda()
                        .eq(DmsTwQueue::getIsFinish,"0")
                        .eq(DmsTwQueue::getIsLock,"0")
                        .or()
                        .eq(DmsTwQueue::getIsLock,"2")
                        .orderBy(true,true,DmsTwQueue::getType)
                        .orderBy(true,true,DmsTwQueue::getCreateTime);
                List<DmsTwQueue> dmsTwQueues = dmsTwQueueService.list(dmsTwQueueQuery);
                if(CollUtil.isNotEmpty(dmsTwQueues)){

                    DmsTwQueue dmsTwQueue = dmsTwQueues.get(0);
                    Integer requestId = dmsTwQueue.getRequestId();
                    String processCode = dmsTwQueue.getProcessCode();

                    QueryWrapper<DmsTwLock> twLockQuery = new QueryWrapper<>();
                    //查询isLock:0 已释放锁的记录，查到有就证明曾经占用过锁，对记录状态进行更新
                    twLockQuery.lambda().eq(DmsTwLock::getRequestId,requestId).eq(DmsTwLock::getIsLock,"0");
                    DmsTwLock exsitDmsTwLock = dmsTwLockService.getOne(twLockQuery);
                    DmsTwLock dmsTwLock = new DmsTwLock();
                    boolean result = false;
                    if(exsitDmsTwLock != null){
                        processCode = exsitDmsTwLock.getProcessCode();
                        log.info("requestId="+requestId+",请求编号="+processCode+",曾经占用过锁，目前是isLock是已释放锁状态，目前马上进行加锁");

                        UpdateWrapper<DmsTwLock> twLockUpdate = new UpdateWrapper<>();
                        twLockUpdate.lambda().set(DmsTwLock::getIsLock,"1").eq(DmsTwLock::getRequestId,requestId).eq(DmsTwLock::getLockType,"0");
                        result = dmsTwLockService.update(twLockUpdate);

                        log.info("更新结果为,result="+result);
                    }else {
                        processCode = dmsTwQueue.getProcessCode();
                        log.info("requestId="+requestId+",请求编号="+processCode+",未占用过锁，目前马上进行加锁");

                        dmsTwLock.setProcessCode(processCode);
                        LocalDateTime today = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String createTime = today.format(formatter);
                        dmsTwLock.setRequestId(requestId);
                        dmsTwLock.setCreateTime(createTime);
                        dmsTwLock.setUpdateTime(createTime);
                        dmsTwLock.setLockType("0");
                        dmsTwLock.setIsLock("1");
                        result = dmsTwLockService.save(dmsTwLock);
                        log.info("新增结果为,result="+result);
                    }

                    if (result == true){
                        log.info("requestId="+requestId+",请求编号="+processCode+",该流程占用锁成功");
                        log.info("对"+"requestId="+requestId+",请求编号="+processCode+"的队列记录进行更新");

                        LocalDateTime updateDay = LocalDateTime.now();
                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String updateTime = updateDay.format(formatter1);


                        UpdateWrapper<DmsTwQueue> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.lambda().eq(DmsTwQueue::getRequestId,dmsTwQueue.getRequestId())
                                // isLock = 1 : 已占用锁
                                .set(DmsTwQueue::getIsLock,"1")
                                //isFinish = 1 : 同步中, 0:未同步,1:占用锁成功同步中,2:同步成功,3:同步失败,4:占用所失败
                                .set(DmsTwQueue::getIsFinish,"1")
                                .set(DmsTwQueue::getUpdateTime,updateTime);

                        dmsTwQueueService.update(updateWrapper);

                        log.info("对该流程进行提交");

                        //提交流程
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("requestId", Integer.valueOf(requestId));
                        DmsUtil.testRegist(dmsConfig.getIp());
                        DmsUtil.testGetoken(dmsConfig.getIp());
                        DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());

                    }else {
                        log.info("requestId="+requestId+",请求编号="+processCode+"该流程占用锁失败");

                        log.info("对"+"requestId="+requestId+",请求编号="+processCode+"的队列记录进行更新");

                        LocalDateTime updateDay = LocalDateTime.now();
                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String updateTime = updateDay.format(formatter1);


                        UpdateWrapper<DmsTwQueue> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.lambda().eq(DmsTwQueue::getRequestId,dmsTwQueue.getRequestId())
                                // isLock = 2 : 占用锁失败
                                .set(DmsTwQueue::getIsLock,"2")
                                //isFinish = 0 : 同步中 ,0:未同步,1:占用锁成功同步中,2:同步成功,3:同步失败,4:占用所失败
                                .set(DmsTwQueue::getIsFinish,"0")
                                .set(DmsTwQueue::getUpdateTime,updateTime);

                        dmsTwQueueService.update(updateWrapper);
                    }
                }else {
                    log.info("队列没数据，结束getLock定时任务");
                }
            }else {
                log.info("目前锁被用了，不提交流程");
            }
        }else {
            log.info("开关已关闭，提交程序不可用");
        }
        log.info("getTwLock(台湾)定时任务执行完毕");
    }
}
