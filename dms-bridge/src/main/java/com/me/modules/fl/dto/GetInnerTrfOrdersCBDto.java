package com.me.modules.fl.dto;


import com.me.modules.fl.pojo.InnerTrfCBItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetInnerTrfOrdersCBDto {

    private String title;

    private String status;

    List<InnerTrfCBItem> items = new ArrayList<>();

}
