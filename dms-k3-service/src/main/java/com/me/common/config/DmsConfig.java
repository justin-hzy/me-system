package com.me.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class DmsConfig {


    @Value("${dms.ip}")
    private String ip;

    @Value("${dms.url}")
    private String url;

    @Value("${dms.order.orderMainTable}")
    private String orderMainTable;

    @Value("${dms.order.orderDetailTable2}")
    private String orderDetailTable2;

    @Value("${dms.order.returnOrderMainTable}")
    private String returnOrderMainTable;

    @Value("${dms.order.returnOrderDetailTable2}")
    private String returnOrderDetailTable2;

    @Value("${dms.order.newReturnOrderDetailTable2}")
    private String newReturnOrderDetailTable2;

    @Value("${dms.order.trfOrderDetailTable2}")
    private String trfOrderDetailTable2;

    @Value("${dms.order.purInMainTable}")
    private String purInMainTable;

    @Value("${dms.order.purInDetailTable2}")
    private String purInDetailTable2;

    @Value("${dms.order.rePurDtlTable2}")
    private String rePurDtlTable2;

    @Value("${dms.order.setDtlTable3}")
    private String setDtlTable3;

    @Value("${dms.order.tranCodeDtlTable2}")
    private String tranCodeDtlTable2;

}
