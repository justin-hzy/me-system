package com.me.modules.bi.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TableColumn {
    private String column_id;

    //暂时不用 注释
    //private String column_type;

    private String name;

    //暂时不用 注释
    //private String type;

    //暂时不用 注释
    //private String behavior_type;

    //暂时不用 注释
    //private String is_inside;

    List options = new ArrayList();

    List<ElementColumn> relation_options = new ArrayList();

    List son_column_bos = new ArrayList();

}
