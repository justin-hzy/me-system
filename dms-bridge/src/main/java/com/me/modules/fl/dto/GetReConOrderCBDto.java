package com.me.modules.fl.dto;

import com.me.modules.fl.pojo.ReConsOrderItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetReConOrderCBDto {

    private String title;

    private String status;

    List<ReConsOrderItem> items = new ArrayList<>();
}
