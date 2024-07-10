package com.me.modules.dms.sys.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;

public interface SampleMaterialService {

    Workbook setData(String requestId, InputStream templateStream) throws IOException;
}
