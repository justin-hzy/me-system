package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.entity.*;
import com.me.modules.dms.sys.service.*;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class SampleOrderServiceImpl implements SampleOrderService {

    private FormTableMain153Service formTableMain153Service;

    private FormTableMain153Dt1Service formTableMain153Dt1Service;

    private UfCityService ufCityService;

    private UfProvinceService ufProvinceService;

    private UfCountyService ufCountyService;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {

        QueryWrapper<FormTableMain153> main153Query = new QueryWrapper<>();
        main153Query.eq("requestid", requestId);
        FormTableMain153 resObj1 = formTableMain153Service.getOne(main153Query);

        String id = resObj1.getId();

        QueryWrapper<FormTableMain153Dt1> main153Dt1Query = new QueryWrapper<>();
        main153Dt1Query.eq("mainid", id);
        List<FormTableMain153Dt1> resList1 = formTableMain153Dt1Service.list(main153Dt1Query);

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
                FormTableMain153Dt1 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);
                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j) {
                        case 0:
                            if("0".equals(data.getLx())){
                                data.setLx("样品");
                            }else if("1".equals(data.getLx())){
                                data.setLx("中小样");
                            }else if("2".equals(data.getLx())){
                                data.setLx("物料");
                            }
                            newCell.setCellValue(data.getLx());
                            break;
                        case 1:
                            newCell.setCellValue(data.getHpbh());
                            break;
                        case 2:
                            newCell.setCellValue(data.getHpmc());
                            break;
                        case 3:
                            newCell.setCellValue(data.getGg());
                            break;
                        case 4:
                            newCell.setCellValue(data.getLsj());
                            break;
                        case 5:
                            newCell.setCellValue(data.getDw());
                            break;
                        case 6:
                            newCell.setCellValue(data.getDdl());
                            break;
                        case 7:
                            newCell.setCellValue(data.getZlsje());
                            break;
                        case 8:
                            newCell.setCellValue(data.getFhdc());
                            break;
                        case 9:
                            newCell.setCellValue(data.getShrdh());
                            break;
                        case 10:
                            newCell.setCellValue(data.getShr());
                            break;
                        case 11:
                            newCell.setCellValue(data.getShrdh());
                            break;
                        case 12:
                            QueryWrapper<UfProvince> queryProvince = new QueryWrapper<>();
                            queryProvince.eq("id",data.getProvince());
                            UfProvince ufProvince = ufProvinceService.getOne(queryProvince);
                            newCell.setCellValue(ufProvince.getProvince());
                            break;
                        case 13:
                            QueryWrapper<UfCity> queryCity = new QueryWrapper<>();
                            queryCity.eq("id",data.getCity());
                            UfCity ufCity = ufCityService.getOne(queryCity);
                            newCell.setCellValue(ufCity.getCity());
                            break;
                        case 14:
                            QueryWrapper<UfCounty> queryCounty = new QueryWrapper<>();
                            queryCounty.eq("id",data.getCounty());
                            UfCounty ufCounty = ufCountyService.getOne(queryCounty);
                            newCell.setCellValue(ufCounty.getCounty());
                            break;
                        case 15:
                            newCell.setCellValue(data.getXxdz());
                            break;
                        case 16:
                            newCell.setCellValue(data.getShdz());
                            break;
                        case 17:
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
