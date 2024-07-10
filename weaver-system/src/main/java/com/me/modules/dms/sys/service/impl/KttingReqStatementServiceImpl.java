package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.entity.FormTableMain177;
import com.me.modules.dms.sys.entity.FormTableMain177Dt1;
import com.me.modules.dms.sys.service.FormTableMain177Dt1Service;
import com.me.modules.dms.sys.service.FormTableMain177Service;
import com.me.modules.dms.sys.service.KttingReqStatementService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class KttingReqStatementServiceImpl implements KttingReqStatementService {

    private FormTableMain177Service formTableMain177Service;

    private FormTableMain177Dt1Service formTableMain177Dt1Service;


    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {

        QueryWrapper<FormTableMain177> main177Query = new QueryWrapper<>();
        main177Query.eq("requestid", requestId);
        FormTableMain177 resObj1 = formTableMain177Service.getOne(main177Query);

        String id = resObj1.getId();

        QueryWrapper<FormTableMain177Dt1> main177Dt1Query = new QueryWrapper<>();
        main177Dt1Query.eq("mainid", id);
        List<FormTableMain177Dt1> resList1 = formTableMain177Dt1Service.list(main177Dt1Query);

        // 创建工作簿对象
        Workbook workbook = WorkbookFactory.create(templateStream);

        if (resList1.size() > 0) {
            // 获取第一个工作表
            Sheet sheet0 = workbook.getSheetAt(0);
            // 获取表头行
            Row headerRow0 = sheet0.getRow(0);
            // 在表头下方插入新的数据行
            int newRowNum0 = sheet0.getLastRowNum() + 1;

            for (int i = 0; i < resList1.size(); i++) {
                FormTableMain177Dt1 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);

                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j) {
                        case 0:
                            newCell.setCellValue(data.getBombm());
                            break;
                        case 1:
                            newCell.setCellValue(data.getFxwlbm());
                            break;
                        case 2:
                            newCell.setCellValue(data.getFxwlmc());
                            break;
                        case 3:
                            newCell.setCellValue(data.getSpbm());
                            break;
                        case 4:
                            newCell.setCellValue(data.getSpmc());
                            break;
                        case 5:
                            newCell.setCellValue(data.getGg());
                            break;
                        case 6:
                            newCell.setCellValue(data.getDw());
                            break;
                        case 7:
                            newCell.setCellValue(data.getSl());
                            break;
                        case 8:
                            newCell.setCellValue(data.getJhztsl());
                            break;
                        case 9:
                            newCell.setCellValue(data.getKykc());
                            break;
                    }
                }
            }
        }
        return workbook;
    }
}
