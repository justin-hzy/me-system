package com.me.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ResponseUtil {
    private ResponseUtil() {

    }

    /**
     * 使用response输出JSON
     *
     * @param response  response
     * @param resultMap resultMap
     */
    public static void out(HttpServletResponse response, Map<String, Object> resultMap) {

        ServletOutputStream out;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            out = response.getOutputStream();
            out.write(JSONUtil.toJsonStr(resultMap).getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("使用response输出JSON异常：", e);
        }
    }

    public static Map<String, Object> resultMap(boolean flag, String code, String msg) {

        return resultMap(flag, code, msg, null);
    }

    public static Map<String, Object> resultMap(boolean flag, String code, String msg, Object data) {

        Map<String, Object> resultMap = new HashMap<>(16);
        resultMap.put("success", flag);
        resultMap.put("message", msg);
        resultMap.put("code", code);
        resultMap.put("timestamp", System.currentTimeMillis());
        if (data != null) {
            resultMap.put("result", data);
        }
        return resultMap;
    }

    /**
     * 下载目录下的文件
     *
     * @param response    response
     * @param outFileName 输出文件名
     * @throws IOException IOException
     */
    public static void download(HttpServletResponse response, String outFileName, String fileType) throws IOException {
        FileUtil.writeToStream(outFileName + "." + fileType, getOutputStream(response, outFileName, fileType));
    }

    /**
     * 设置response属性并返回输出流，用于Excel下载
     *
     * @param response    response
     * @param outFileName 输出文件名
     * @throws IOException IOException
     */
    public static ServletOutputStream getOutputStream(HttpServletResponse response, String outFileName, String fileType) throws IOException {
        String contentType = "";
        String zipType = "zip";
        String excel07Type = "xlsx";
        String excel03Type = "xls";
        String pdfType = "pdf";
        if (zipType.equals(fileType)) {
            contentType = "x-zip-compressed";
        }
        if (excel07Type.equals(fileType) || excel03Type.equals(fileType)) {
            contentType = "vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        if (pdfType.equals(fileType)) {
            contentType = "pdf";
        }
        String fileName = outFileName + "." + fileType;
        response.setContentType("application/" + contentType + ";charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        return response.getOutputStream();
    }
}
