package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.me.modules.dms.sys.entity.FormTableMain148;
import com.me.modules.dms.sys.entity.FormTableMain148Dt1;
import com.me.modules.dms.sys.service.DlStatementService;
import com.me.modules.dms.sys.service.FormTableMain148Dt1Service;
import com.me.modules.dms.sys.service.FormTableMain148Service;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class DlStatementServiceImpl implements DlStatementService {

    private FormTableMain148Service formTableMain148Service;

    private FormTableMain148Dt1Service formTableMain148Dt1Service;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {
        QueryWrapper<FormTableMain148> main148Query = new QueryWrapper<>();
        main148Query.eq("requestid",requestId);
        FormTableMain148 resObj1 = formTableMain148Service.getOne(main148Query);
        String id = resObj1.getId();
        QueryWrapper<FormTableMain148Dt1> main148Dt1Query = new QueryWrapper<>();
        main148Dt1Query.eq("mainid",id);
        List<FormTableMain148Dt1> resList = formTableMain148Dt1Service.list(main148Dt1Query);

        // 创建工作簿对象
        Workbook workbook = WorkbookFactory.create(templateStream);

        // 获取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);

        // 获取表头行
        Row headerRow = sheet.getRow(0);

        // 在表头下方插入新的数据行
        int newRowNum = sheet.getLastRowNum() + 1;

        for(int i =0;i<resList.size();i++){
            FormTableMain148Dt1 data = resList.get(i);
            Row dataRow = sheet.createRow(newRowNum + i);

            String lx = data.getLx();
            if("0".equals(lx)){
                data.setLx("发货");
            }
            if("1".equals(lx)){
                data.setLx("退货");
            }
            if("2".equals(lx)){
                data.setLx("收款");
            }
            if("3".equals(lx)){
                data.setLx("付款");
            }
            if("4".equals(lx)){
                data.setLx("货补");
            }
            if("5".equals(lx)){
                data.setLx("临时额度");
            }

            String dDlx = data.getDdlx();
            if("0".equals(dDlx)){
                data.setDdlx("线下销售(正常订单)");
            }
            if("1".equals(dDlx)){
                data.setDdlx("线下销售(补货 )");
            }


            // 遍历表头单元格，并在新行中创建对应的单元格，并填充数据
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                Cell newCell = dataRow.createCell(j);
                switch (j){
                    case 0:
                        newCell.setCellValue(data.getLx());
                        break;
                    case 1:
                        newCell.setCellValue(data.getSyfllx());
                        break;
                    case 2:
                        newCell.setCellValue(data.getHpmc());
                        break;
                    case 3:
                        newCell.setCellValue(data.getHpmc01());
                        break;
                    case 4:
                        newCell.setCellValue(data.getRq());
                        break;
                    case 5:
                        newCell.setCellValue(data.getDw());
                        break;
                    case 6:
                        newCell.setCellValue(data.getGg());
                        break;
                    case 7:
                        newCell.setCellValue(data.getHpsl());
                        break;
                    case 8:
                        newCell.setCellValue(data.getDpzfje());
                        break;
                    case 9:
                        newCell.setCellValue(data.getHjje());
                        break;
                    case 10:
                        newCell.setCellValue(data.getYdzsl());
                        break;
                    case 11:
                        newCell.setCellValue(data.getWdzsl());
                        break;
                    case 12:
                        newCell.setCellValue(data.getDzsl());
                        break;
                    case 13:
                        newCell.setCellValue(data.getDdlx());
                        break;
                    default:
                        newCell.setCellValue("匹配数据失败");
                }
            }
        }
        return workbook;
    }
}