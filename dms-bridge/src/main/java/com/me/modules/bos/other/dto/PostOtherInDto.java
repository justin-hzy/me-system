package com.me.modules.bos.other.dto;

import com.me.modules.bos.other.pojo.SubOther;
import lombok.Data;

import java.util.List;

@Data
public class PostOtherInDto {

    private String sku;

    private String qty;

    private List<SubOther> subOthers;

    private String description;

    private String billDate;

    private String cstore;

    private String requestId;
}
