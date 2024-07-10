package com.me.modules.k3.assembly.dto;

import com.me.modules.k3.assembly.jsonpojo.AssyFEntityJson;
import com.me.modules.k3.assembly.pojo.AssyFEntity;
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
