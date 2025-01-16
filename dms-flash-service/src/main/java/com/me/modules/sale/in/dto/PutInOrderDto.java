package com.me.modules.sale.in.dto;

import com.me.modules.sale.in.pojo.InGood;
import lombok.Data;

import java.util.List;

@Data
public class PutInOrderDto {

    private String warehouseId;

    private String storeCode;

    private String type;

    private String qualityStatus;

    private String orderSn;

    private String channelSource;

    private String deliveryWay;

    private String supplyCode;

    private String carrier;

    private String deliveryMan;

    private String plateNumber;

    private String deliveryNumber;

    private String deliveryContact;

    private String remark;

    private String arrivalStart;

    private String arrivalEnd;

    private String check;

    private String ignorePrice;

    private String isBatchArrival;

    List<InGood> goods;

}
