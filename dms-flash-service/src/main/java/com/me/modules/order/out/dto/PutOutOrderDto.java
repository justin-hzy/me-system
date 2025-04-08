package com.me.modules.order.out.dto;

import com.me.modules.order.out.pojo.OutGood;
import lombok.Data;

import java.util.List;

@Data
public class PutOutOrderDto {

    private String status;

    private String warehouseId;

    private String type;

    private String channelSource;

    private String nodeSn;

    private String consigneeName;

    private String consigneePhone;

    private String province;

    private String city;

    private String district;

    private String postalCode;

    private String consigneeAddress;

    private String deliveryWay;

    private String outTime;

    private String goodsStatus;

    private String remark;

    private String markName;

    private String orderSn;

    private String storeCode;

    private List<OutGood> goods;
}
