package com.me.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("K3_M_PURCHASEITEM")
public class K3MPurchaseItem {

    @TableField("ID")
    private Long id;

    @TableField("BOS_ID")
    private Long bosId;

    @TableField("MAIN_ID")
    private Long mainId;

    @TableField("EDIFLAG")
    private Long ediFlag = 80L;

    @TableField("WRITEDATE")
    private String writeDate;

    @TableField("RESULTS")
    private String results;

    @TableField("FMATERIALID")
    private String fmaterialId;

    @TableField("FREALQTY")
    private String frealQty;

    @TableField("FSTOCKID")
    private String fstockId;

    @TableField("FGIVEAWAY")
    private String fgiveaway;

    @TableField("FENTRYTAXRATE")
    private String fentryTaxRate;

    @TableField("FTAXPRICE")
    private String ftaxPrice;

    @TableField("FNOTE")
    private String fnote;

    @TableField("F_DH_ENTRYSOURCENO")
    private String fdhEntrySourceNo;

    @TableField("API_DOCNO")
    private String apiDocNo;

    @TableField("FSRCBILLNO")
    private String fSrcBillNo;

    @TableField("FINSTOCKENTRY_LINK_FSID")
    private String finstockEntryLinkFsid;

    @TableField("FINSTOCKENTRY_LINK_FSBILLID")
    private String finstockEntryLinkFsbillId;

    @TableField("FINSTOCKENTRY_LINK_FSTABLENAME")
    private String finstockEntryLinkFstablename;

    @TableField("FINSTOCKENTRY_LINK_FRULEID")
    private String finstockEntryLinkFruleId;

    @TableField("FSRCTYPE")
    private String fsrcType;

    @TableField("F_ZJM_AMOUNT_BJCLCB")
    private Integer fzjmAmountBjclcb = 0;

    @TableField("F_ZJM_TEXT_HTH")
    private String fzjmTextHth;

    @TableField("FENTRYID")
    private String fentryId;

    @TableField("T_PUR_RECEIVEENTRY")
    private String tpurReceiveEntry;

    @TableField("FSRCFORMID")
    private String fsrcFormId;

    @TableField("FPOORDERNO")
    private String fpoOrderNo;


}
