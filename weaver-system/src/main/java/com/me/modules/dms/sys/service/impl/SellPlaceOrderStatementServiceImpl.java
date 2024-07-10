package com.me.modules.dms.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.sys.entity.FormTableMain138;
import com.me.modules.dms.sys.entity.FormTableMain138Dt1;
import com.me.modules.dms.sys.entity.FormTableMain138Dt2;
import com.me.modules.dms.sys.entity.FormTableMain138Dt4;
import com.me.modules.dms.sys.service.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class SellPlaceOrderStatementServiceImpl implements SellPlaceOrderStatementService {

    private FormTableMain138Service formTableMain138Service;

    private FormTableMain138Dt1Service formTableMain138Dt1Service;

    private FormTableMain138Dt4Service formTableMain138Dt4Service;

    private FormTableMain138Dt2Service formTableMain138Dt2Service;

    @Override
    public Workbook setData(String requestId, InputStream templateStream) throws IOException {
        QueryWrapper<FormTableMain138> main138Query = new QueryWrapper<>();
        main138Query.eq("requestid",requestId);
        FormTableMain138 resObj1 = formTableMain138Service.getOne(main138Query);
        String id = resObj1.getId();

        QueryWrapper<FormTableMain138Dt1> main138Dt1Query = new QueryWrapper<>();
        main138Dt1Query.eq("mainid",id);
        List<FormTableMain138Dt1> resList1 = formTableMain138Dt1Service.list(main138Dt1Query);

        QueryWrapper<FormTableMain138Dt4> main138Dt4Query = new QueryWrapper<>();
        main138Dt4Query.eq("mainid",id);
        List<FormTableMain138Dt4> resList2 = formTableMain138Dt4Service.list(main138Dt4Query);

        QueryWrapper<FormTableMain138Dt2> main138Dt2Query = new QueryWrapper<>();
        main138Dt2Query.eq("mainid",id);
        List<FormTableMain138Dt2> resList3 = formTableMain138Dt2Service.list(main138Dt2Query);

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
                FormTableMain138Dt1 data = resList1.get(i);
                Row dataRow = sheet0.createRow(newRowNum0 + i);
                // 遍历表头单元格，并在新行中创建对应的单元格，并填充数据
                for (int j = 0; j < headerRow0.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j){
                        case 0:
                            if(StrUtil.isEmpty(data.getHptxm())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getHptxm());
                            }
                            break;
                        case 1:
                            if(StrUtil.isEmpty(data.getHpmc())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getHpmc());
                            }
                            break;
                        case 2:
                            if(StrUtil.isEmpty(data.getGg())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getGg());
                            }
                            break;
                        case 3:
                            if(StrUtil.isEmpty(data.getDw())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getDw());
                            }
                            break;
                        case 4:
                            if(data.getLsj() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getLsj());
                            }
                            break;
                        case 5:
                            if(data.getZk() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getZk());
                            }
                            break;
                        case 6:
                            if(data.getDdje() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getDdje());
                            }
                            break;
                        case 7:
                            if(StrUtil.isEmpty(data.getDdl())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getDdl());
                            }
                            break;
                        case 8:
                            if(StrUtil.isEmpty(data.getKykc())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getKykc());
                            }
                            break;
                        case 9:
                            if(StrUtil.isEmpty(data.getFhl())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getFhl());
                            }
                            break;
                        case 10:
                            if(StrUtil.isEmpty(data.getShsl())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getShsl());
                            }
                            break;
                        case 11:
                            if(data.getZlsje() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getZlsje());
                            }
                            break;
                        case 12:
                            if(data.getZfhje() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getZfhje());
                            }
                            break;
                        case 13:
                            if(data.getZshje() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getZshje());
                            }
                            break;
                        case 14:
                            if(StrUtil.isEmpty(data.getXsckdh())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getXsckdh());
                            }
                            break;
                        case 15:
                            if(StrUtil.isEmpty(data.getChrq())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getChrq());
                            }
                            break;
                        case 16:
                            if(StrUtil.isEmpty(data.getWlgs())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getWlgs());
                            }
                            break;
                        case 17:
                            if(StrUtil.isEmpty(data.getWldh())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getWldh());
                            }
                            break;
                        case 18:
                            if("0".equals(data.getSfcyyf())){
                                data.setSfcyyf("是");
                            }else if("1".equals(data.getSfcyyf())){
                                data.setSfcyyf("否");
                            }
                            newCell.setCellValue(data.getSfcyyf());
                            break;
                        case 19:
                            if("0".equals(data.getSfcyjf())){
                                data.setSfcyjf("是");
                            }else if("1".equals(data.getSfcyjf())){
                                data.setSfcyjf("否");
                            }
                            newCell.setCellValue(data.getSfcyjf());
                            break;
                        case 20:
                            if("0".equals(data.getSfcynf())){
                                data.setSfcynf("是");
                            }else if("1".equals(data.getSfcynf())){
                                data.setSfcynf("否");
                            }
                            newCell.setCellValue(data.getSfcynf());
                            break;
                        default:
                            newCell.setCellValue("匹配数据失败");
                    }
                }
            }
        }

        if(resList2.size()>0){
            Sheet sheet1 = workbook.getSheetAt(1);
            Row headerRow1 = sheet1.getRow(0);
            int newRowNum1 = sheet1.getLastRowNum() + 1;
            for(int i=0;i<resList2.size();i++){
                FormTableMain138Dt4 data = resList2.get(i);
                Row dataRow = sheet1.createRow(newRowNum1 + i);
                for (int j = 0; j < headerRow1.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j){
                        case 0:
                            if(StrUtil.isEmpty(data.getHptxm())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getHptxm());
                            }
                            break;
                        case 1:
                            if(StrUtil.isEmpty(data.getHpmc())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getHpmc());
                            }
                            break;
                        case 2:
                            if(StrUtil.isEmpty(data.getGg())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getGg());
                            }
                            break;
                        case 3:
                            if(StrUtil.isEmpty(data.getDw())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getDw());
                            }
                            break;
                        case 4:
                            if(data.getLsj() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getLsj());
                            }
                            break;
                        case 5:
                            if(data.getDdl() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getDdl());
                            }
                            break;
                        case 6:
                            if(data.getKykc() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getKykc());
                            }
                            break;
                        case 7:
                            if(data.getFhl() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getFhl());
                            }
                            break;
                        case 8:
                            if(data.getShsl() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getShsl());
                            }
                            break;
                        case 9:
                            if(StrUtil.isEmpty(data.getXsckdh())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getXsckdh());
                            }
                            break;
                        case 10:
                            if(StrUtil.isEmpty(data.getChrq())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getChrq());
                            }
                            break;
                        case 11:
                            if(StrUtil.isEmpty(data.getWlgs())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getWlgs());
                            }
                            break;
                        case 12:
                            if(StrUtil.isEmpty(data.getWldh())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getWldh());
                            }
                            break;
                        default:
                            newCell.setCellValue("匹配数据失败");
                    }
                }
            }
        }

        if(resList3.size()>0){
            Sheet sheet2 = workbook.getSheetAt(2);
            Row headerRow2 = sheet2.getRow(0);
            int newRowNum2 = sheet2.getLastRowNum() + 1;

            for(int i=0;i<resList3.size();i++){
                FormTableMain138Dt2 data = resList3.get(i);
                Row dataRow = sheet2.createRow(newRowNum2 + i);
                for (int j = 0; j < headerRow2.getLastCellNum(); j++) {
                    Cell newCell = dataRow.createCell(j);
                    switch (j){
                        case 0:
                            if(data.getLx() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getLx());
                            }
                            break;
                        case 1:
                            if(StrUtil.isEmpty(data.getHptxm())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getHptxm());
                            }
                            break;
                        case 2:
                            if(StrUtil.isEmpty(data.getHpmc())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getHpmc());
                            }
                            break;
                        case 3:
                            if(StrUtil.isEmpty(data.getGg())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getGg());
                            }
                            break;
                        case 4:
                            if(StrUtil.isEmpty(data.getDw())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getDw());
                            }
                            break;
                        case 5:
                            if(data.getLsj() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getLsj());
                            }
                            break;
                        case 6:
                            if(data.getDdl() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getDdl());
                            }
                            break;
                        case 7:
                            if(data.getKykc() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getKykc());
                            }
                            break;
                        case 8:
                            if(data.getFhl() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getFhl());
                            }
                            break;
                        case 9:
                            if(data.getShsl() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getShsl());
                            }
                            break;
                        case 10:
                            if(data.getZsjelsj() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getZsjelsj());
                            }
                            break;
                        case 11:
                            if(data.getFhjelsj() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getFhjelsj());
                            }
                            break;
                        case 12:
                            if(data.getShje() == null){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getShje());
                            }
                            break;
                        case 13:
                            if("1".equals(data.getYpth())){
                                data.setYpth("否");
                            }else if("0".equals(data.getYpth())){
                                data.setYpth("是");
                            }
                            newCell.setCellValue(data.getYpth());
                            break;
                        case 14:
                            if(StrUtil.isEmpty(data.getXsckdh())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getXsckdh());
                            }
                            break;
                        case 15:
                            if(StrUtil.isEmpty(data.getChrq())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getChrq());
                            }
                            break;
                        case 16:
                            if(StrUtil.isEmpty(data.getWlgs())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getWlgs());
                            }
                            break;
                        case 17:
                            if(StrUtil.isEmpty(data.getWldh())){
                                newCell.setCellValue("");
                            }else {
                                newCell.setCellValue(data.getWldh());
                            }
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
