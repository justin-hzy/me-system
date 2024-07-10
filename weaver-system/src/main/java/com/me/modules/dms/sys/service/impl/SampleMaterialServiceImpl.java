package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.entity.FormTableMain144;
import com.me.modules.dms.sys.entity.FormTableMain144Dt1;
import com.me.modules.dms.sys.service.FormTableMain144Dt1Service;
import com.me.modules.dms.sys.service.FormTableMain144Service;
import com.me.modules.dms.sys.service.SampleMaterialService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class SampleMaterialServiceImpl implements SampleMaterialService {

    private FormTableMain144Service formTableMain144Service;

    private FormTableMain144Dt1Service formTableMain144Dt1Service;


    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {

        QueryWrapper<FormTableMain144> main144Query = new QueryWrapper<>();
        main144Query.eq("requestid", requestId);
        FormTableMain144 resObj1 = formTableMain144Service.getOne(main144Query);

        String id = resObj1.getId();


        QueryWrapper<FormTableMain144Dt1> main144Dt1Query = new QueryWrapper<>();
        main144Dt1Query.eq("mainid", id);
        List<FormTableMain144Dt1> resList1 = formTableMain144Dt1Service.list(main144Dt1Query);

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
                FormTableMain144Dt1 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);
                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j) {
                        case 0:
                            newCell.setCellValue(data.getZlcbh());
                            break;
                        case 1:
                            if("0".equals(data.getLx())){
                                data.setLx("样品");
                            }else if("1".equals(data.getLx())){
                                data.setLx("中小样");
                            }else if("2".equals(data.getLx())){
                                data.setLx("物料");
                            }
                            newCell.setCellValue(data.getLx());
                            break;
                        case 2:
                            newCell.setCellValue(data.getHpbh());
                            break;
                        case 3:
                            newCell.setCellValue(data.getHpmc());
                            break;
                        case 4:
                            newCell.setCellValue(data.getGg());
                            break;
                        case 5:
                            newCell.setCellValue(data.getDw());
                            break;
                        case 6:
                            newCell.setCellValue(data.getDrfl());
                            break;
                        case 7:
                            if("0".equals(data.getPp())){
                                data.setPp("姬芮");
                            }else if("1".equals(data.getPp())){
                                data.setPp("泊美");
                            }
                            newCell.setCellValue(data.getPp());
                            break;
                        case 8:
                            newCell.setCellValue(data.getCp());
                            break;
                        case 9:
                            newCell.setCellValue(data.getFhrq());
                            break;
                        case 10:
                            newCell.setCellValue(data.getXsckdh());
                            break;
                        case 11:
                            newCell.setCellValue(data.getWldh());
                            break;
                        case 12:
                            newCell.setCellValue(data.getWlgs());
                            break;
                        case 13:
                            newCell.setCellValue(data.getLsj());
                            break;
                        case 14:
                            newCell.setCellValue(data.getDdl());
                            break;
                        case 15:
                            newCell.setCellValue(data.getZsjelsj());
                            break;
                        case 16:
                            newCell.setCellValue(data.getXdzje());
                            break;
                        case 17:
                            newCell.setCellValue(data.getCksl());
                            break;
                        case 18:
                            newCell.setCellValue(data.getFhzje());
                            break;
                        case 19:
                            newCell.setCellValue(data.getShsl());
                            break;
                        case 20:
                            newCell.setCellValue(data.getShzje());
                            break;
                        case 21:
                            newCell.setCellValue(data.getShr());
                            break;
                        case 22:
                            newCell.setCellValue(data.getShrdh());
                            break;
                        case 23:
                            newCell.setCellValue(data.getShdz());
                            break;
                        case 24:
                            newCell.setCellValue(data.getBz());
                            break;
                        case 25:
                            if("0".equals(data.getYpth())){
                                data.setYpth("是");
                            }else if("1".equals(data.getYpth())){
                                data.setYpth("否");
                            }
                            newCell.setCellValue(data.getYpth());
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
