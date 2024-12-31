package com.me.modules.refund.dto;

import com.me.modules.refund.pojo.RefundGood;
import lombok.Data;

import java.util.List;

@Data
public class PutRefundDto {

    private String externalOrderSn;

    private String backType;

    private String backReason;

    private String deliverySn;

    private String externalUserInfo;

    private String senderAddress;

    private String province;

    private String city;

    private String district;

    private String postalCode;

    private String senderName;

    private String senderPhone;

    private String carrier;

    private String backExpressSn;

    private String oriExpressSn;

    private String weight;

    private String size;

    private String buyerRemark;

    private String backPayMode;

    private String backStatus;

    private String bankId;

    private String payee;

    private String bankName;

    private String warehouseId;

    private String ignorePrice;

    private List<RefundGood> goods;
}
