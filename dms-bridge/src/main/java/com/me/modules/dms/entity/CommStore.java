package com.me.modules.dms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_spk")
public class CommStore {

    /*货品编码*/
    private String hpbh;

    /*货品名称*/
    private String hpmc;

    /*所属组织*/
    private String szzt;
}
