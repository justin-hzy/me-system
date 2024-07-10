package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.entity.FormTableMain142;
import com.me.modules.dms.sys.entity.FormTableMain142Dt1;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import com.me.modules.dms.sys.service.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class AOStatementServiceImpl implements AOStatementService {

    private FormTableMain142Service formTableMain142Service;

    private FormTableMain142Dt1Service formTableMain142Dt1Service;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {

        QueryWrapper<FormTableMain142> main142Query = new QueryWrapper<>();
        main142Query.eq("requestid", requestId);
        FormTableMain142 resObj1 = formTableMain142Service.getOne(main142Query);

        String id = resObj1.getId();

        QueryWrapper<FormTableMain142Dt1> main153Dt1Query = new QueryWrapper<>();
        main153Dt1Query.eq("mainid", id);
        List<FormTableMain142Dt1> resList1 = formTableMain142Dt1Service.list(main153Dt1Query);

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
                FormTableMain142Dt1 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);
                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j) {
                        case 0:
                            newCell.setCellValue(data.getHptxm());
                            break;
                        case 1:
                            newCell.setCellValue(data.getHpbh());
                            break;
                        case 2:
                            newCell.setCellValue(data.getSpmc());
                            break;
                        case 3:
                            if("137".equals(data.getXslb())){
                                data.setXslb("原材料");
                            }else if("134".equals(data.getXslb())){
                                data.setXslb("外购赠品");
                            }else if("132".equals(data.getXslb())){
                                data.setXslb("大中小样品");
                            }else if("103".equals(data.getXslb())){
                                data.setXslb("物料");
                            }else if("97".equals(data.getXslb())){
                                data.setXslb("销售品");
                            }else if("71".equals(data.getXslb())){
                                data.setXslb("中试品");
                            }else if("67".equals(data.getXslb())){
                                data.setXslb("其他");
                            }
                            newCell.setCellValue(data.getXslb());
                            break;
                        case 4:
                            newCell.setCellValue(data.getGg());
                            break;
                        case 5:
                            newCell.setCellValue(data.getBzj().toString());
                            break;
                        case 6:
                            newCell.setCellValue(data.getDbsl());
                            break;
                        case 7:
                            newCell.setCellValue(data.getBzje());
                            break;
                        case 8:
                            newCell.setCellValue(data.getBz());
                            break;
                        case 9:
                            newCell.setCellValue(data.getCksl().toString());
                            break;
                        case 10:
                            newCell.setCellValue(data.getCksl().multiply(data.getBzj()).toString());
                            break;
                        case 11:
                            newCell.setCellValue(data.getRksl().toString());
                            break;
                        case 12:
                            newCell.setCellValue(data.getRksl().multiply(data.getBzj()).toString());
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
