package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_county")
public class UfCounty {

    @TableField("id")
    private String id;

    @TableField("requestId")
    private String requestId;

    @TableField("formmodeid")
    private String formmodeid;

    @TableField("modedatacreater")
    private String modedatacreater;

    @TableField("modedatacreatertype")
    private String modedatacreatertype;

    @TableField("modedatacreatedate")
    private String modedatacreatedate;

    @TableField("modedatacreatetime")
    private String modedatacreatetime;

    @TableField("modedatamodifier")
    private String modedatamodifier;

    @TableField("modedatamodifydatetime")
    private String modedatamodifydatetime;

    @TableField("form_biz_id")
    private String form_biz_id;

    @TableField("MODEUUID")
    private String MODEUUID;

    @TableField("county")
    private String county;

    @TableField("city")
    private String city;
}
