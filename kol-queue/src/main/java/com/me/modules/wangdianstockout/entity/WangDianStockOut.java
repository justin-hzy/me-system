package com.me.modules.wangdianstockout.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("WANGDIAN_STOCKOUT")
public class WangDianStockOut {
    
    @TableField("ID")
    @ApiModelProperty("出库单主键id")
    private Long id;

    @TableField("STOCKOUTID")
    @ApiModelProperty("出库单号")
    private String stockOutId;

    @TableField("ORDERNO")
    @ApiModelProperty("订单号")
    private String orderNo;

    @TableField("SRCORDERNO")
    @ApiModelProperty("源单号")
    private String srcOrderNo;

    @TableField("WAREHOUSENO")
    @ApiModelProperty("仓库编号")
    private String warehouseNo;

    @TableField("CONSIGNTIME")
    @ApiModelProperty("发货时间")
    private String consignTime;

    @TableField("ORDERTYPE")
    @ApiModelProperty("出库单类型")
    private String orderType;

    @TableField("TRADESTATUS")
    @ApiModelProperty("订单状态")
    private String tradeStatus;

    @TableField("ORDERTYPENAME")
    @ApiModelProperty("出库单类型名称")
    private String orderTypeName;

    @TableField("TRADETYPE")
    @ApiModelProperty("订单类型")
    private String tradeType;

    @TableField("SUBTYPE")
    @ApiModelProperty("出库子类型")
    private String subType;

    @TableField("GOODSCOUNT")
    @ApiModelProperty("数量")
    private String goodsCount;

    @TableField("GOODSTOTALAMOUNT")
    @ApiModelProperty("货品总售价")
    private String goodsTotalAmount;

    @TableField("GOODSTOTALCOST")
    @ApiModelProperty("货品总成本")
    private String goodsTotalCost;

    @TableField("POSTFEE")
    @ApiModelProperty("邮资成本")
    private String postFee;

    @TableField("LOGISTICSNO")
    @ApiModelProperty("物流单号")
    private String logisticsNo;

    @TableField("PACKAGEFEE")
    @ApiModelProperty("包装费")
    private String packageFee;

    @TableField("RECEIVERNAME")
    @ApiModelProperty("收件人")
    private String receiverName;

    @TableField("RECEIVERCOUNTRY")
    @ApiModelProperty("国家")
    private String receiverCountry;

    @TableField("RECEIVERPROVINCE")
    @ApiModelProperty("省份")
    private String receiverProvince;

    @TableField("RECEIVERPROVINCECODE")
    @ApiModelProperty("收件人的省份")
    private String receiverProvinceCode;

    @TableField("RECEIVERCITY")
    @ApiModelProperty("城市")
    private String receiverCity;

    @TableField("RECEIVERCITYCODE")
    @ApiModelProperty("收件人的城市")
    private String receiverCityCode;

    @TableField("RECEIVERDISTRICT")
    @ApiModelProperty("地区")
    private String receiverDistrict;

    @TableField("RECEIVERDISTRICTCODE")
    @ApiModelProperty("收件人的地区")
    private String receiverDistrictCode;

    @TableField("RECEIVABLE")
    @ApiModelProperty("应收金额")
    private String receivable;

    @TableField("RECEIVERADDRESS")
    @ApiModelProperty("详细地址")
    private String receiverAddress;

    @TableField("RECEIVERMOBILE")
    @ApiModelProperty("收件人移动电话")
    private String receiverMobile;

    @TableField("RECEIVERTELNO")
    @ApiModelProperty("收件人固定电话")
    private String receiverTelNo;

    @TableField("RECEIVERZIP")
    @ApiModelProperty("邮编")
    private String receiverZip;

    @TableField("RECEIVERDTB")
    @ApiModelProperty("大头笔")
    private String receiverDTB;

    @TableField("STOCKOUTRECEIVERDTB")
    @ApiModelProperty("大头笔")
    private String stockOutReceiverDTB;

    @TableField("WEIGHT")
    @ApiModelProperty("实际重量")
    private String weight;

    @TableField("LOGISTICSTYPE")
    @ApiModelProperty("物流公司类型")
    private String logisticsType;

    @TableField("LOGISTICSCODE")
    @ApiModelProperty("物流公司编号")
    private String logisticsCode;

    @TableField("LOGISTICSNAME")
    @ApiModelProperty("物流公司名称")
    private String logisticsName;

    @TableField("LOGISTICSTEMPLATEID")
    @ApiModelProperty("物流单模板id")
    private String logisticsTemplateId;

    @TableField("PRINTREMARK")
    @ApiModelProperty("打印备注")
    private String printRemark;

    @TableField("PAID")
    @ApiModelProperty("已付金额")
    private String paid;

    @TableField("REFUNDSTATUS")
    @ApiModelProperty("退款状态")
    private String refundStatus;

    @TableField("SALESMANNO")
    @ApiModelProperty("业务员编号")
    private String salesmanNo;

    @TableField("SALESMANNAME")
    @ApiModelProperty("业务员名称")
    private String salesmanName;

    @TableField("FULLNAME")
    @ApiModelProperty("业务员名称")
    private String fullName;

    @TableField("WAREHOUSENAME")
    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @TableField("CREATED")
    @ApiModelProperty("创建时间")
    private String created;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    @TableField("STOCKOUTREASON")
    @ApiModelProperty("出库原因")
    private String stockOutReason;

    @TableField("OUTERNO")
    @ApiModelProperty("外部单号")
    private String outerNo;

    @TableField("TRADENO")
    @ApiModelProperty("订单编号")
    private String tradeNo;

    @TableField("SRCTRADENO")
    @ApiModelProperty("原始单号")
    private String srcTradeNo;

    @TableField("NICKNAME")
    @ApiModelProperty("网名")
    private String nickname;

    @TableField("CUSTOMERNAME")
    @ApiModelProperty("客户名称")
    private String customerName;

    @TableField("CUSTOMERNO")
    @ApiModelProperty("客户编号")
    private String customerNo;

    @TableField("TRADETIME")
    @ApiModelProperty("交易时间")
    private String tradeTime;

    @TableField("PAYTIME")
    @ApiModelProperty("付款时间")
    private String payTime;

    @TableField("STATUS")
    @ApiModelProperty("出库单状态")
    private String status;

    @TableField("SHOPNAME")
    @ApiModelProperty("店铺名称")
    private String shopName;

    @TableField("SHOPNO")
    @ApiModelProperty("店铺编号")
    private String shopNo;

    @TableField("BUYERMESSAGE")
    @ApiModelProperty("买家留言")
    private String buyerMessage;

    @TableField("CSREMARK")
    @ApiModelProperty("客服备注")
    private String csRemark;

    @TableField("FLAGNAME")
    @ApiModelProperty("订单标记")
    private String flagName;

    @TableField("POSTAMOUNT")
    @ApiModelProperty("邮费")
    private String postAmount;

    @TableField("BLOCKREASON")
    @ApiModelProperty("截停原因")
    private String blockReason;

    @ApiModelProperty(value = "发票类别")
    @TableField("INVOICETYPE")
    private String invoiceType;

    @ApiModelProperty(value = "发票抬头")
    @TableField("INVOICETITLE")
    private String invoiceTitle;

    @ApiModelProperty(value = "发票内容")
    @TableField("INVOICECONTENT")
    private String invoiceContent;

    @ApiModelProperty(value = "发票ID")
    @TableField("INVOICEID")
    private String invoiceId;

    @ApiModelProperty(value = "发货条件")
    @TableField("DELIVERYTERM")
    private String deliveryTerm;

    @ApiModelProperty(value = "分销类别")
    @TableField("FENXIAOTYPE")
    private String fenxiaoType;

    @ApiModelProperty(value = "分销商信息")
    @TableField("FENXIAONICK")
    private String fenxiaoNick;

    @ApiModelProperty(value = "货到付款金额")
    @TableField("CODAMOUNT")
    private String codAmount;

    @ApiModelProperty(value = "证件类别")
    @TableField("IDCARDTYPE")
    private String idCardType;

    @ApiModelProperty(value = "收件人地址")
    @TableField("RECEIVERAREA")
    private String receiverArea;

    @ApiModelProperty(value = "店铺备注")
    @TableField("SHOPREMARK")
    private String shopRemark;

    @ApiModelProperty(value = "最后修改时间")
    @TableField("MODIFIED")
    private String modified;

    @ApiModelProperty(value = "平台id")
    @TableField("PLATFORMID")
    private String platformId;

    @ApiModelProperty(value = "订单表主键")
    @TableField("TRADEID")
    private String tradeId;

    @ApiModelProperty(value = "审单员")
    @TableField("CHECKERNAME")
    private String checkerName;

    @ApiModelProperty(value = "审单员编号")
    @TableField("EMPLOYEENO")
    private String employeeNo;

    @ApiModelProperty(value = "打包员编号")
    @TableField("PACKAGERNO")
    private String packagerNo;

    @ApiModelProperty(value = "打包员")
    @TableField("PACKAGERNAME")
    private String packagerName;

    @ApiModelProperty(value = "拣货员编号")
    @TableField("PICKERNO")
    private String pickerNo;

    @ApiModelProperty(value = "拣货员")
    @TableField("PICKERNAME")
    private String pickerName;

    @ApiModelProperty(value = "打单员编号")
    @TableField("PRINTERNO")
    private String printerNo;

    @ApiModelProperty(value = "打单员")
    @TableField("PRINTERNAME")
    private String printerName;

    @ApiModelProperty(value = "验货员编号")
    @TableField("EXAMINERNO")
    private String examinerNo;

    @ApiModelProperty(value = "验货员")
    @TableField("EXAMINERNAME")
    private String examinerName;

    @ApiModelProperty(value = "出库单号")
    @TableField("STOCKOUTNO")
    private String stockOutNo;

    @ApiModelProperty(value = "源单据类别")
    @TableField("SRCORDERTYPE")
    private String srcOrderType;

    @ApiModelProperty(value = "处理状态")
    @TableField("WMSSTATUS")
    private String wmsStatus;

    @ApiModelProperty(value = "接口处理错误信息")
    @TableField("ERRORINFO")
    private String errorInfo;

    @ApiModelProperty(value = "仓库类别")
    @TableField("WAREHOUSETYPE")
    private String warehouseType;

    @ApiModelProperty(value = "出库仓库id")
    @TableField("WAREHOUSEID")
    private String warehouseId;

    @ApiModelProperty(value = "客户id")
    @TableField("CUSTOMERID")
    private String customerId;

    @ApiModelProperty(value = "冻结原因")
    @TableField("FREEZEREASON")
    private String freezeReason;

    @ApiModelProperty(value = "是否分配")
    @TableField("ISALLOCATED")
    private String isAllocated;

    @ApiModelProperty(value = "出库状态")
    @TableField("CONSIGNSTATUS")
    private String consignStatus;

    @ApiModelProperty(value = "电子面单状态")
    @TableField("EBILLSTATUS")
    private String eBillStatus;

    @ApiModelProperty(value = "制单人")
    @TableField("OPERATORID")
    private String operatorId;

    @ApiModelProperty(value = "货品种类数量")
    @TableField("GOODSTYPECOUNT")
    private String goodsTypeCount;

    @ApiModelProperty(value = "MD5字符串值")
    @TableField("MD5STR")
    private String md5Str;

    @ApiModelProperty(value = "内置字段")
    @TableField("RAWGOODSCOUNT")
    private String rawGoodsCount;

    @ApiModelProperty(value = "其他出库自定义子类别")
    @TableField("CUSTOMTYPE")
    private String customType;

    @ApiModelProperty(value = "区域")
    @TableField("RECEIVERRING")
    private String receiverRing;

    @ApiModelProperty(value = "配送时间")
    @TableField("TODELIVERTIME")
    private String toDeliverTime;

    @ApiModelProperty(value = "配送时间")
    @TableField("PRECHARGETIME")
    private String preChargeTime;

    @ApiModelProperty(value = "物流公司ID")
    @TableField("LOGISTICSID")
    private String logisticsId;

    @ApiModelProperty(value = "未知成本销售总额")
    @TableField("UNKNOWNGOODSAMOUNT")
    private String unknownGoodsAmount;

    @ApiModelProperty(value = "预估邮费成本")
    @TableField("CALCPOSTCOST")
    private String calcPostCost;

    @ApiModelProperty(value = "邮费成本")
    @TableField("POSTCOST")
    private String postCost;

    @ApiModelProperty(value = "预估重量")
    @TableField("CALCWEIGHT")
    private String calcWeight;

    @ApiModelProperty(value = "快递重量")
    @TableField("POSTWEIGHT")
    private String postWeight;

    @ApiModelProperty(value = "包装id")
    @TableField("PACKAGEID")
    private String packageId;

    @ApiModelProperty(value = "包装成本")
    @TableField("PACKAGECOST")
    private String packageCost;

    @ApiModelProperty(value = "是否包含发票")
    @TableField("HASINVOICE")
    private String hasInvoice;

    @ApiModelProperty(value = "打单员")
    @TableField("PRINTERID")
    private String printerId;

    @ApiModelProperty(value = "拣货出错次数")
    @TableField("PICKERRORCOUNT")
    private String pickErrorCount;

    @ApiModelProperty(value = "拣货员id")
    @TableField("PICKERID")
    private String pickerId;

    @ApiModelProperty(value = "分拣员id")
    @TableField("SORTERID")
    private String sorterId;

    @ApiModelProperty(value = "验货员id")
    @TableField("EXAMINERID")
    private String examinerId;

    @ApiModelProperty(value = "发货人id")
    @TableField("CONSIGNERID")
    private String consignerId;

    @ApiModelProperty(value = "打包员id")
    @TableField("PACKAGERID")
    private String packagerId;

    @ApiModelProperty(value = "打包积分")
    @TableField("PACKSCORE")
    private String packScore;

    @ApiModelProperty("拣货积分")
    @TableField("PICKSCORE")
    private String PICKSCORE;

    @ApiModelProperty("签出员工id")
    @TableField("CHECKOUTERID")
    private String CHECKOUTERID;

    @ApiModelProperty("监视员id")
    @TableField("WATCHERID")
    private String WATCHERID;

    @ApiModelProperty("分拣单编号")
    @TableField("PICKLISTNO")
    private String PICKLISTNO;

    @ApiModelProperty("分拣序号")
    @TableField("PICKLISTSEQ")
    private String PICKLISTSEQ;

    @ApiModelProperty("发票打印状态")
    @TableField("INVOICEPRINTSTATUS")
    private String INVOICEPRINTSTATUS;

    @ApiModelProperty("颜色")
    @TableField("FLAGID")
    private String FLAGID;

    @ApiModelProperty("物流单模板id")
    @TableField("LOGISTICSTEMPLATEID")
    private String LOGISTICSTEMPLATEID;

    @ApiModelProperty("发货单模板id")
    @TableField("SENDBILLTEMPLATEID")
    private String SENDBILLTEMPLATEID;

    @ApiModelProperty("货位分配方式")
    @TableField("POSALLOCATEMODE")
    private String POSALLOCATEMODE;

    @ApiModelProperty("便签条数")
    @TableField("NOTECOUNT")
    private String NOTECOUNT;

    @ApiModelProperty("其他出库原因")
    @TableField("REASONID")
    private String REASONID;

    @ApiModelProperty("锁定策略id")
    @TableField("LOCKID")
    private String LOCKID;

    @ApiModelProperty("保留")
    @TableField("RESERVE")
    private String RESERVE;

    @ApiModelProperty("打印批次")
    @TableField("BATCHNO")
    private String BATCHNO;

    @ApiModelProperty("优惠金额")
    @TableField("DISCOUNT")
    private String DISCOUNT;

    @ApiModelProperty("原始单号")
    @TableField("SRCTIDS")
    private String SRCTIDS;

    @ApiModelProperty("税额")
    @TableField("TAX")
    private BigDecimal TAX;

    @ApiModelProperty("税率")
    @TableField("TAXRATE")
    private BigDecimal TAXRATE;

    @ApiModelProperty("币种")
    @TableField("CURRENCY")
    private String CURRENCY;

    @ApiModelProperty("证件号码")
    @TableField("IDCARD")
    private String IDCARD;

    @ApiModelProperty("出库单审核时间")
    @TableField("STOCKCHECKTIME")
    private String STOCKCHECKTIME;

    @ApiModelProperty("异常状态")
    @TableField("BADREASON")
    private String BADREASON;

    @ApiModelProperty("打印批次")
    @TableField("PRINTBATCHNO")
    private String PRINTBATCHNO;

    @ApiModelProperty("发货单打印状态")
    @TableField("SENDBILLPRINTSTATUS")
    private String SENDBILLPRINTSTATUS;

    @ApiModelProperty("物流单打印状态")
    @TableField("LOGISTICSPRINTSTATUS")
    private String LOGISTICSPRINTSTATUS;

    @ApiModelProperty("分拣单打印状态")
    @TableField("PICKLISTPRINTSTATUS")
    private String PICKLISTPRINTSTATUS;

    @ApiModelProperty("制单人名称")
    @TableField("OPERATORNAME")
    private String OPERATORNAME;

    @ApiModelProperty("结果")
    @TableField("RESULTS")
    private String RESULTS;




}
