package com.me.modules.fl.dto;

import com.me.modules.fl.pojo.ProcessItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetProcessCBDto {

    private String title;

    private String status;

    private List<ProcessItem> items;
}
