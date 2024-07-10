package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.entity.FormTableMain167;
import com.me.modules.dms.sys.entity.FormTableMain167Dt2;
import com.me.modules.dms.sys.service.BtchRetReqStatementService;
import com.me.modules.dms.sys.service.FormTableMain167Dt2Service;
import com.me.modules.dms.sys.service.FormTableMain167Service;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class BtchRetReqStatementServiceImpl implements BtchRetReqStatementService {


    private FormTableMain167Service formTableMain167Service;

    private FormTableMain167Dt2Service formTableMain167Dt2Service;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {

        QueryWrapper<FormTableMain167> main167Query = new QueryWrapper<>();
        main167Query.eq("requestid", requestId);
        FormTableMain167 resObj1 = formTableMain167Service.getOne(main167Query);

        String id = resObj1.getId();

        QueryWrapper<FormTableMain167Dt2> main167Dt2Query = new QueryWrapper<>();
        main167Dt2Query.eq("mainid", id);
        List<FormTableMain167Dt2> resList1 = formTableMain167Dt2Service.list(main167Dt2Query);

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
                FormTableMain167Dt2 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);

                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j) {
                        case 0:
                            newCell.setCellValue(data.getHptxm());
                            break;
                        case 1:
                            newCell.setCellValue(data.getHpmc());
                            break;
                        case 2:
                            newCell.setCellValue(data.getDw());
                            break;
                        case 3:
                            newCell.setCellValue(data.getLsj());
                            break;
                        case 4:
                            newCell.setCellValue(data.getYddzk());
                            break;
                        case 5:
                            newCell.setCellValue(data.getThsl());
                            break;
                        case 6:
                            newCell.setCellValue(data.getYdhje());
                            break;
                        case 7:
                            newCell.setCellValue(data.getLsje());
                            break;
                        case 8:
                            newCell.setCellValue(data.getCpxq());
                            break;
                        case 9:
                            newCell.setCellValue(data.getYxsdh());
                            break;
                    }
                }
            }
        }
        return workbook;
    }
}
