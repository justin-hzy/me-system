package com.me.modules.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dms_tw_queue")
public class DmsTwQueue {

    @TableField("request_id")
    private Integer requestId;

    @TableField("process_code")
    private String processCode;

    @TableField("type")
    private String type;

    @TableField("warehouse")
    private String warehouse;

    //0:未同步,1:占用锁成功同步中,2:同步成功,3:同步失败,4:占用所失败
    @TableField("is_lock")
    private String isLock;

    @TableField("is_finish")
    private String isFinish;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;
}
