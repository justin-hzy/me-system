package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.service.FormTableMain140Dt1Service;
import com.me.modules.dms.sys.service.FormTableMain140Service;
import com.me.modules.dms.sys.service.SupplementGoodsStatementService;
import com.me.modules.dms.sys.entity.FormTableMain140;
import com.me.modules.dms.sys.entity.FormTableMain140Dt1;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class SupplementGoodsStatementServiceImpl implements SupplementGoodsStatementService {

    private FormTableMain140Service formTableMain140Service;

    private FormTableMain140Dt1Service formTableMain140Dt1Service;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {
        QueryWrapper<FormTableMain140> main140Query = new QueryWrapper<>();
        main140Query.eq("requestid",requestId);
        FormTableMain140 resObj1 = formTableMain140Service.getOne(main140Query);
        String id = resObj1.getId();

        QueryWrapper<FormTableMain140Dt1> main140Dt1Query = new QueryWrapper<>();
        main140Dt1Query.eq("mainid",id);
        List<FormTableMain140Dt1> resList1 = formTableMain140Dt1Service.list(main140Dt1Query);

        // 创建工作簿对象
        Workbook workbook = WorkbookFactory.create(templateStream);

        if(resList1.size()>0){
            // 获取第一个工作表
            Sheet sheet0 = workbook.getSheetAt(0);
            // 获取表头行
            Row headerRow0 = sheet0.getRow(0);
            // 在表头下方插入新的数据行
            int newRowNum0 = sheet0.getLastRowNum() + 1;
            for(int i =0;i<resList1.size();i++){
                FormTableMain140Dt1 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);
                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j){
                        case 0:
                            newCell.setCellValue(data.getHptxm());
                            break;
                        case 1:
                            newCell.setCellValue(data.getHpmc());
                            break;
                        case 2:
                            newCell.setCellValue(data.getGg());
                            break;
                        case 3:
                            newCell.setCellValue(data.getDw());
                            break;
                        case 4:
                            newCell.setCellValue(data.getLsj());
                            break;
                        case 5:
                            newCell.setCellValue(data.getZk());
                            break;
                        case 6:
                            newCell.setCellValue(data.getDdje());
                            break;
                        case 7:
                            newCell.setCellValue(data.getDdl());
                            break;
                        case 8:
                            newCell.setCellValue(data.getFhl());
                            break;
                        case 9:
                            newCell.setCellValue(data.getShsl());
                            break;
                        case 10:
                            newCell.setCellValue(data.getZlsje());
                            break;
                        case 11:
                            newCell.setCellValue(data.getZfhje());
                            break;
                        case 12:
                            newCell.setCellValue(data.getZshje());
                            break;
                        default:
                            newCell.setCellValue("匹配数据失败");
                    }
                }
            }
        }
        return workbook;
    }
}
