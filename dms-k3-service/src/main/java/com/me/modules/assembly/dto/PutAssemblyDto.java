package com.me.modules.assembly.dto;

import com.me.modules.assembly.jsonpojo.AssyFEntityJson;
import com.me.modules.assembly.pojo.AssyFEntity;
import lombok.Data;

import java.util.List;

@Data
public class PutAssemblyDto {

    private String fillno;

    private String fstockorgid;

    private String faffairtype;

    private String fdate;

    private List<AssyFEntity> assyFEntities;



}
