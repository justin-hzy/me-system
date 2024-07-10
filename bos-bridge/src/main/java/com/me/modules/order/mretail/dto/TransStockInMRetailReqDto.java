package com.me.modules.order.mretail.dto;

import lombok.Data;

@Data
public class TransStockInMRetailReqDto {

    Integer v_origstore_id;

    Integer v_customer_id;

    Integer v_count;

    Integer v_deststore_id;

    String v_sku;

    Integer v_m_transfer_id;
}
