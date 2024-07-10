package com.me.modules.dms.sys.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.me.common.enums.ResultCode;
import com.me.common.exception.BusinessException;
import com.me.common.utils.ResponseUtil;
import com.me.modules.dms.sys.service.ExcelSetDataService;
import com.me.modules.dms.sys.service.FormTableMain147Dt1Service;
import com.me.modules.dms.sys.service.FormTableMain147Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("dms")
@Slf4j
@AllArgsConstructor
//@CrossOrigin(origins = "http://120.77.244.137:8080",methods = {RequestMethod.GET,RequestMethod.POST})
//@CrossOrigin(origins = "http://39.108.136.182:8080",methods = {RequestMethod.GET,RequestMethod.POST})
@CrossOrigin
public class DmsController {

    private FormTableMain147Service formTableMain147Service;

    private FormTableMain147Dt1Service formTableMain147Dt1Service;

    private ExcelSetDataService excelSetDataService;

    @GetMapping("export")
    public void export(HttpServletResponse response,@RequestParam("requestId") String requestId,@RequestParam("fileName")String fileName) throws IOException {
        //log.info("requestId=" + requestId);
        String fileType = "xls";
        ServletOutputStream out = ResponseUtil.getOutputStream(response, fileName, fileType);
        InputStream templateStream = ResourceUtil.getStream("file/" + fileName + "." + fileType);

        Workbook workbook = excelSetDataService.setData(requestId,templateStream,fileName);
        if (workbook == null){
            throw new BusinessException(ResultCode.ExcelFail.getCode(),ResultCode.ExcelFail.getMessage());
        }
        // 将修改后的工作簿写入输出流
        workbook.write(out);
        // 关闭资源
        workbook.close();
        IoUtil.flush(out);
        IoUtil.close(out);
    }

    @GetMapping("test")
    public String test(){
        return "hello world!";
    }




}
