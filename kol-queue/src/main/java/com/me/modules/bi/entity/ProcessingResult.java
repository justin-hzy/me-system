package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName(value = "processing_result")
@ApiModel(description = "处理结果实体类")
public class ProcessingResult {

    private String id;

    private String description;
}
