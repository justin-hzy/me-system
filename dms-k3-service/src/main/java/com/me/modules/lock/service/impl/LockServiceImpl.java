package com.me.modules.lock.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.core.JsonResult;
import com.me.modules.dms.entity.DmsTwSaleOrder;
import com.me.modules.dms.service.DmsTwSaleOrderService;
import com.me.modules.lock.dto.UnTwLockReqDto;
import com.me.modules.lock.entity.DmsTwLock;
import com.me.modules.lock.service.DmsTwLockService;
import com.me.modules.lock.service.LockService;
import com.me.modules.queue.entity.DmsTwQueue;
import com.me.modules.queue.service.DmsTwQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class LockServiceImpl implements LockService {

    private DmsTwLockService dmsTwLockService;

    private DmsTwQueueService dmsTwQueueService;

    private DmsTwSaleOrderService dmsTwSaleOrderService;

    @Override
    public JsonResult unLock(UnTwLockReqDto dto) {
        JsonResult jsonResult = new JsonResult();
        Integer requestId = dto.getRequestId();

        QueryWrapper<DmsTwLock> dmsTwLockQuery = new QueryWrapper<>();
        dmsTwLockQuery.lambda().eq(DmsTwLock::getRequestId,requestId).eq(DmsTwLock::getIsLock,"1");

        try {
            DmsTwLock dmsTwLock = dmsTwLockService.getOne(dmsTwLockQuery);

            if(dmsTwLock != null){
                LocalDateTime updateDay = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String updateTime = updateDay.format(formatter);

                UpdateWrapper<DmsTwLock> dmsTwLockUpdate = new UpdateWrapper<>();
                dmsTwLockUpdate.lambda().set(DmsTwLock::getIsLock,"0")
                        .set(DmsTwLock::getUpdateTime,updateTime)
                        .eq(DmsTwLock::getRequestId,requestId);

                boolean result = dmsTwLockService.update(dmsTwLockUpdate);

                if (result == true){
                    log.info("requestId="+requestId+",释放锁成功");
                    //查询当前记录是否存在与队列中且 is_finish : 1 (同步中),is_lock : 1 (已占用锁)
                    QueryWrapper<DmsTwQueue> dmsTwQueueQuery = new QueryWrapper<>();
                    dmsTwQueueQuery.lambda().eq(DmsTwQueue::getIsFinish,"1")
                            .eq(DmsTwQueue::getIsLock,"1")
                            .eq(DmsTwQueue::getRequestId,requestId);

                    DmsTwQueue dmsTwQueue  = dmsTwQueueService.getOne(dmsTwQueueQuery);

                    if(dmsTwQueue != null){


                        QueryWrapper<DmsTwSaleOrder> dmsTwSaleOrderQuery = new QueryWrapper<>();
                        dmsTwSaleOrderQuery.lambda().eq(DmsTwSaleOrder::getRequestId,requestId);
                        DmsTwSaleOrder dmsTwSaleOrder = dmsTwSaleOrderService.getOne(dmsTwSaleOrderQuery);
                        if (dmsTwSaleOrder != null){
                            String isHkEnough = dmsTwSaleOrder.getIsHkEnough();
                            String isTwEnough = dmsTwSaleOrder.getIsTwEnough();

                            if("1".equals(isHkEnough) || "1".equals(isTwEnough)){
                                //更新当前记录是否存在与队列中  is_finish : 4 (库存不足),is_lock : 3 (已释放锁)
                                UpdateWrapper<DmsTwQueue> dmsTwQueueUpdate = new UpdateWrapper<>();
                                dmsTwQueueUpdate.lambda().set(DmsTwQueue::getIsFinish,"4")
                                        .set(DmsTwQueue::getIsLock,"3")
                                        .eq(DmsTwQueue::getRequestId,dmsTwQueue.getRequestId());
                                dmsTwQueueService.update(dmsTwQueueUpdate);
                            }else {
                                //更新当前记录是否存在与队列中  is_finish : 2 (同步成功),is_lock : 3 (已释放锁)
                                UpdateWrapper<DmsTwQueue> dmsTwQueueUpdate = new UpdateWrapper<>();
                                dmsTwQueueUpdate.lambda().set(DmsTwQueue::getIsFinish,"2")
                                        .set(DmsTwQueue::getIsLock,"3")
                                        .eq(DmsTwQueue::getRequestId,dmsTwQueue.getRequestId());
                                dmsTwQueueService.update(dmsTwQueueUpdate);
                            }

                            String note = "requestId="+requestId+",同步金蝶成功已释放锁";
                            log.info(note);
                            jsonResult.setCode("200");
                            jsonResult.setMessage(note);
                        }else {
                            String note = "requestId="+requestId+",DMS流程数据错乱,请联系IT部门,释放锁失败";
                            log.info(note);
                            jsonResult.setCode("500");
                            jsonResult.setMessage(note);
                        }
                    }else {
                        String note = "requestId="+requestId+",没找到相关堆栈记录,请联系IT部门,释放锁失败";
                        log.info(note);
                        jsonResult.setCode("500");
                        jsonResult.setMessage(note);
                    }
                }else {
                    String note = "requestId="+requestId+",释放锁失败";
                    log.info(note);
                    jsonResult.setCode("500");
                    jsonResult.setMessage(note);
                }
            }else {
                String note = "requestId="+requestId+",没找到相关占用锁记录,请联系IT部门";
                log.info(note);
                jsonResult.setCode("500");
                jsonResult.setMessage(note);
            }
        }catch (TooManyResultsException e){
            String note = "requestId="+requestId+",占用两个台湾金蝶锁";
            log.info(note);
            jsonResult.setCode("500");
            jsonResult.setMessage(note);
        }


        return jsonResult;
    }
}
