package com.me.modules.sale.dto;

import com.me.modules.sale.pojo.SaleGood;
import lombok.Data;

import java.util.List;

@Data
public class PutSaleOrderDto {

    private String companyCode;

    private String sfOrderType;

    private String erpOrder;

    private String destinationFacilityAliasId;

    private String orderNote;

    private String tradeOrderDateTime;

    private String receiverCompany;

    private String receiverName;

    private String receiverMobile;

    private String receiverCountry;

    private String receiverAddress;

    private String senderCompany;

    private String senderName;

    private List<SaleGood> saleGoods;
}
