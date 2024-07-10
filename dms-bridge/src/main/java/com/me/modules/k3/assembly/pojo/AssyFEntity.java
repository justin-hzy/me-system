package com.me.modules.k3.assembly.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AssyFEntity {

    private String fmaterialid;

    private String fqty;

    private String fstockid;

    private String frefbomid;


    private List<AssyFSubEntity> assyFSubEntities;


}
