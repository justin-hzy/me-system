package com.me.modules.nascent.point.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("nascent_offline_member_point_log")
public class PointLog {

    @Id
    private Long id;

    @TableField("nas_ouid")
    private String nasOuid;

    @TableField("point")
    private BigDecimal point;

    @TableField("point_createtime")
    private Date pointCreateTime;

    @TableField("request_id")
    private String requestId;

    @TableField("type")
    private Integer type;

    @TableField("provider_guid")
    private String providerGUID;

    @TableField("is_bos")
    private String isBos;

}
