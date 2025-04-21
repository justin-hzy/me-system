package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("formtable_main_147")
public class FormTableMain147 {

    @TableField("id")
    @ApiModelProperty("id")
    private String id;

    @TableField("requestid")
    @ApiModelProperty("requestid")
    private String requestid;

    @TableField("lcbh")
    @ApiModelProperty("流程编号")
    private String lcbh;

    @TableField("sqr")
    @ApiModelProperty("申请人")
    private String sqr;

    @TableField("sqrq")
    @ApiModelProperty("申请日期")
    private String sqrq;

    @TableField("szbm")
    @ApiModelProperty("所属部门")
    private String szbm;

    @TableField("szgs")
    @ApiModelProperty("所属公司")
    private String szgs;

    @TableField("kh")
    @ApiModelProperty("客户")
    private String kh;

    @TableField("khfz")
    @ApiModelProperty("客户分组")
    private String khfz;

    @TableField("sqdj")
    @ApiModelProperty("申请单据")
    private String sqdj;

    @TableField("sfxyjs")
    @ApiModelProperty("是否需要寄送")
    private String sfxyjs;

    @TableField("sjr")
    @ApiModelProperty("收件人")
    private String sjr;

    @TableField("sjrdh")
    @ApiModelProperty("收件人电话")
    private String sjrdh;

    @TableField("sjrdz")
    @ApiModelProperty("收件人地址")
    private String sjrdz;

    @TableField("khdzdsfxgz")
    private String khdzdsfxgz;

    @TableField("khdzdsc")
    private String khdzdsc;

    @TableField("bz")
    @ApiModelProperty("备注")
    private String bz;

    @TableField("biz")
    private String biz;

    @TableField("zdszzq")
    @ApiModelProperty("账单所属周期")
    private String zdszzq;

    @TableField("fhje")
    @ApiModelProperty("发货金额")
    private String fhje;

    @TableField("thje")
    @ApiModelProperty("退货金额")
    private String thje;

    @TableField("ykpje")
    @ApiModelProperty("应开票金额")
    private String ykpje;

    @TableField("hdpk")
    private String hdpk;

    @TableField("ykpjine")
    private String ykpjine;

    @TableField("fyzk")
    private String fyzk;

    @TableField("wkpjne")
    private String wkpjne;

    @TableField("yhfp")
    private String yhfp;

    @TableField("yhk")
    private String yhk;

    @TableField("whfp")
    private String whfp;

    @TableField("yihk")
    private String yihk;

    @TableField("whk")
    private String whk;

    @TableField("dqjl")
    private String dqjl;

    @TableField("fthjlid")
    private String fthjlid;

    @TableField("khjsfs")
    private String khjsfs;

    @TableField("khbh")
    private String khbh;

    @TableField("szzt")
    private String szzt;

    @TableField("xszg")
    private String xszg;

    @TableField("gzlx")
    private String gzlx;

    @TableField("sfyhk")
    private String sfyhk;

    @TableField("sfgxhkhje")
    private String sfgxhkhje;

    @TableField("fp")
    private String fp;

    @TableField("yszksqzq")
    private String yszksqzq;

    @TableField("yszkqcy")
    private String yszkqcy;

    @TableField("bysfytjdzdjl")
    private String bysfytjdzdjl;

}
