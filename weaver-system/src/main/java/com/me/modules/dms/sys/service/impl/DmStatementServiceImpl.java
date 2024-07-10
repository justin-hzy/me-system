package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.entity.FormTableMain147;
import com.me.modules.dms.sys.entity.FormTableMain147Dt1;
import com.me.modules.dms.sys.service.DmStatementService;
import com.me.modules.dms.sys.service.FormTableMain147Dt1Service;
import com.me.modules.dms.sys.service.FormTableMain147Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DmStatementServiceImpl implements DmStatementService {


    private FormTableMain147Service formTableMain147Service;

    private FormTableMain147Dt1Service formTableMain147Dt1Service;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {
        QueryWrapper<FormTableMain147> main147Query = new QueryWrapper<>();
        main147Query.eq("requestid",requestId);
        FormTableMain147 resObj1 = formTableMain147Service.getOne(main147Query);
        String id = resObj1.getId();
        QueryWrapper<FormTableMain147Dt1> main147Dt1Query = new QueryWrapper<>();
        main147Dt1Query.eq("mainid",id);
        List<FormTableMain147Dt1> resList = formTableMain147Dt1Service.list(main147Dt1Query);

        // 创建工作簿对象
        Workbook workbook = WorkbookFactory.create(templateStream);

        // 获取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);

        // 获取表头行
        Row headerRow = sheet.getRow(0);

        // 在表头下方插入新的数据行
        int newRowNum = sheet.getLastRowNum() + 1;

        for(int i =0;i<resList.size();i++){
            FormTableMain147Dt1 data = resList.get(i);
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
                        newCell.setCellValue(data.getHptxm());
                        break;
                    case 3:
                        newCell.setCellValue(data.getHpmc());
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
                        newCell.setCellValue(data.getHjjey());
                        break;
                    case 10:
                        newCell.setCellValue(data.getYdzhpsl());
                        break;
                    case 11:
                        newCell.setCellValue(data.getDdzsl());
                        break;
                    case 12:
                        newCell.setCellValue(data.getDzsl());
                        break;
                    default:
                        newCell.setCellValue("匹配数据失败");
                }
            }
        }
        return workbook;
    }
}
