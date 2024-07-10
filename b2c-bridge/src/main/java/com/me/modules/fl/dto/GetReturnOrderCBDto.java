package com.me.modules.fl.dto;


import com.me.modules.fl.pojo.ReturnOrderItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetReturnOrderCBDto {

    private String title;

    private String status;

    private String note;

    List<ReturnOrderItem> returnOrderItems = new ArrayList<>();

}
