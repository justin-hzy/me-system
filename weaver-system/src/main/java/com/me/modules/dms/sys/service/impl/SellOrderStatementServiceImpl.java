package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.service.FormTableMain152Dt1Service;
import com.me.modules.dms.sys.service.FormTableMain152Dt2Service;
import com.me.modules.dms.sys.service.FormTableMain152Service;
import com.me.modules.dms.sys.service.SellOrderStatementService;
import com.me.modules.dms.sys.entity.FormTableMain152;
import com.me.modules.dms.sys.entity.FormTableMain152Dt1;
import com.me.modules.dms.sys.entity.FormTableMain152Dt2;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class SellOrderStatementServiceImpl implements SellOrderStatementService {

    private FormTableMain152Service formTableMain152Service;

    private FormTableMain152Dt1Service formTableMain152Dt1Service;

    private FormTableMain152Dt2Service formTableMain152Dt2Service;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {
        QueryWrapper<FormTableMain152> main152Query = new QueryWrapper<>();
        main152Query.eq("requestid", requestId);
        FormTableMain152 resObj1 = formTableMain152Service.getOne(main152Query);

        String id = resObj1.getId();

        QueryWrapper<FormTableMain152Dt1> main152Dt1Query = new QueryWrapper<>();
        main152Dt1Query.eq("mainid", id);
        List<FormTableMain152Dt1> resList1 = formTableMain152Dt1Service.list(main152Dt1Query);

        QueryWrapper<FormTableMain152Dt2> main152Dt2Query = new QueryWrapper<>();
        main152Dt2Query.eq("mainid", id);
        List<FormTableMain152Dt2> resList2 = formTableMain152Dt2Service.list(main152Dt2Query);

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
                FormTableMain152Dt1 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);
                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j) {
                        case 0:
                            newCell.setCellValue(data.getPo());
                            break;
                        case 1:
                            newCell.setCellValue(data.getQcsck());
                            break;
                        case 2:
                            newCell.setCellValue(data.getShr());
                            break;
                        case 3:
                            newCell.setCellValue(data.getLxdh());
                            break;
                        case 4:
                            newCell.setCellValue(data.getShdz());
                            break;
                        case 5:
                            newCell.setCellValue(data.getHptxm());
                            break;
                        case 6:
                            newCell.setCellValue(data.getHpmc());
                            break;
                        case 7:
                            newCell.setCellValue(data.getGg());
                            break;
                        case 8:
                            newCell.setCellValue(data.getDw());
                            break;
                        case 9:
                            newCell.setCellValue(data.getDdl());
                            break;
                        case 10:
                            newCell.setCellValue(data.getHsdj());
                            break;
                        case 11:
                            newCell.setCellValue(data.getHszj());
                            break;
                        case 12:
                            newCell.setCellValue(data.getKykc());
                            break;
                        default:
                            newCell.setCellValue("匹配数据失败");
                    }
                }
            }
        }

        if (resList2.size() > 0) {
            // 获取第一个工作表
            Sheet sheet0 = workbook.getSheetAt(1);
            // 获取表头行
            Row headerRow0 = sheet0.getRow(0);
            // 在表头下方插入新的数据行
            int newRowNum0 = sheet0.getLastRowNum() + 1;
            for (int i = 0; i < resList2.size(); i++) {
                FormTableMain152Dt2 data = resList2.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);
                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j) {
                        case 0:
                            newCell.setCellValue(data.getHblx());
                            break;
                        case 1:
                            newCell.setCellValue(data.getPo());
                            break;
                        case 2:
                            newCell.setCellValue(data.getQcsck());
                            break;
                        case 3:
                            newCell.setCellValue(data.getShr());
                            break;
                        case 4:
                            newCell.setCellValue(data.getShrdh());
                            break;
                        case 5:
                            newCell.setCellValue(data.getShdz());
                            break;
                        case 6:
                            newCell.setCellValue(data.getHptxm());
                            break;
                        case 7:
                            newCell.setCellValue(data.getHpmc());
                            break;
                        case 8:
                            newCell.setCellValue(data.getGg());
                            break;
                        case 9:
                            newCell.setCellValue(data.getDw());
                            break;
                        case 10:
                            newCell.setCellValue(data.getDdl());
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
