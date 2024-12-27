package com.me.modules.pur.dto;

import com.me.modules.pur.pojo.RefundGood;
import lombok.Data;

import java.util.List;

@Data
public class PutRefundPurDto {

    private String warehouseCode;

    private String erpOrder;

    private String erpOrderType;

    private String transferWarehouseCode;

    List<RefundGood> goods;

}
