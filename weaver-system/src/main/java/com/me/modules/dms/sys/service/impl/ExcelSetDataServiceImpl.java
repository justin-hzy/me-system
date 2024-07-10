package com.me.modules.dms.sys.service.impl;

import com.me.modules.dms.sys.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
@Slf4j
public class ExcelSetDataServiceImpl implements ExcelSetDataService {

    private DmStatementService dmStatementService;

    private DlStatementService dlStatementService;

    private SellPlaceOrderStatementService sellPlaceOrderStatementService;

    private SupplementGoodsStatementService supplementGoodsStatementService;

    private SellOrderStatementService sellOrderStatementService;

    private SampleMaterialService sampleMaterialService;

    private SampleOrderService sampleOrderService;

    private AOStatementService aoStatementService;

    private BtchRetReqStatementService retReqStatementService;

    private KttingReqStatementService kttingReqStatementService;

    @Override
    public Workbook setData(String requestId, InputStream templateStream,String fileName) throws IOException {

        Workbook workbook = null;
        if(fileName.equals("对账单（经销商场）流程")){
            workbook = dmStatementService.setData(requestId,templateStream);
        }
        if(fileName.equals("对账单（代理商）流程")){
            workbook = dlStatementService.setData(requestId,templateStream);
        }
        if(fileName.equals("销售下单流程（经销商）")){
            workbook = sellPlaceOrderStatementService.setData(requestId,templateStream);
        }
        if(fileName.equals("货补下单流程")){
            workbook = supplementGoodsStatementService.setData(requestId,templateStream);
        }
        if(fileName.equals("销售订单流程")){
            workbook = sellOrderStatementService.setData(requestId,templateStream);
        }
        if(fileName.equals("样品中小样物料下单流程")){
            workbook = sampleMaterialService.setData(requestId,templateStream);
        }
        if(fileName.equals("样品下单流程")){
            workbook = sampleOrderService.setData(requestId,templateStream);
        }
        if(fileName.equals("调拨单流程")){
            workbook = aoStatementService.setData(requestId,templateStream);
        }
        if(fileName.equals("批量退货申请流程")){
            workbook = retReqStatementService.setData(requestId,templateStream);
        }
        if(fileName.equals("组套申请流程")){
            workbook = kttingReqStatementService.setData(requestId,templateStream);
        }
        return workbook;
    }

}
