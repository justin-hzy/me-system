package com.me.modules.upload.controller;

import com.amap.api.services.geocoder.GeocodeSearch;
import com.me.common.core.JsonResult;
import com.me.common.utils.SearchHttpAK;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;


import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("upload")

public class UploadController {


    @Value("${baiduMap.key}")
    private String key;

    @Autowired
    private  RestTemplate restTemplate;


    @PostMapping("commit")
    @CrossOrigin
    public JsonResult commit(@RequestParam("excel_file") MultipartFile excelFile){
        try {
            // 获取上传的文件名
            String fileName = excelFile.getOriginalFilename();
            // 读取Excel文件
           Workbook workbook = WorkbookFactory.create(excelFile.getInputStream());
            /*log.info("执行commit方法");
            log.info("fileName="+fileName);*/
            // 获取第一个 sheet
            Sheet sheet = workbook.getSheetAt(0);

            List<String> addressListFromFile = new ArrayList<>();

            // 遍历每一行
            for (Row row : sheet) {
                // 遍历每一列
                for (Cell cell : row) {
                    if(!"地址（省/市/区）".equals(cell.toString())){
                        addressListFromFile.add(cell.toString());
                    }
                }
            }
            log.info(addressListFromFile.toString());


            return JsonResult.ok("文件上传成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.ok("文件上传失败");
        }

    }

    @GetMapping("testAddress")
    public JsonResult testAddress() throws Exception {
        SearchHttpAK snCal = new SearchHttpAK();
        Map params = new LinkedHashMap<String, String>();
        /*
        * 湖南省长沙市开福区高岭汽配城B区6街2222
        * 山西省太原市万柏林区兴华街道办后北屯社区中奥小区1号楼1单元101
        * 山东省济南市历城区华山街道盖世物流园五库区7-005
        * 河南省郑州市中牟县新安路普洛斯物流园A-4库屈臣氏仓库
        *
        * */
        params.put("address", "河南省郑州市中牟县新安路普洛斯物流园A-4库屈臣氏仓库");
        params.put("ak", snCal.AK);
        params.put("model","1");
        String str = snCal.requestGetAK(snCal.URL, params);
        System.out.println(str);
        return JsonResult.ok();
    }


}
