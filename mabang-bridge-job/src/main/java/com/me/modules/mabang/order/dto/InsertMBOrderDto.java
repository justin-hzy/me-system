package com.me.modules.mabang.order.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InsertMBOrderDto {

    private String platformOrderId;

    private String currencyId;

    private String expressTime;

    private String street;

    private String shopId;

    private String voucherPriceOrigin;

    private String subsidyAmountOrigin;

    private String shippingTotalOrigin;

    private String otherIncome;

    private String itemTotalOrigin;

    private String shopName;

    private String orderStatus;

    private String isTransK3;

    List<InsertMBOrderDtl> insertMBOrderDtls = new ArrayList<>();

}
