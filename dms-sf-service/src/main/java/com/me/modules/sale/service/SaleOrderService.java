package com.me.modules.sale.service;

import com.me.common.core.JsonResult;
import com.me.modules.sale.dto.PutSaleOrderDto;
import com.me.modules.sale.dto.SaleCancelDto;
import com.me.modules.sale.entity.ThSaleOrder;

import java.io.IOException;

public interface SaleOrderService {

    JsonResult putSaleOrder(PutSaleOrderDto dto) throws IOException;

    JsonResult transSaleOrderDtl(ThSaleOrder thSaleOrder) throws IOException;

    JsonResult transSaleOrderDtl(ThSaleOrder thSaleOrder,String delTableName) throws IOException;

    JsonResult saleCancel(SaleCancelDto dto) throws IOException;
}
