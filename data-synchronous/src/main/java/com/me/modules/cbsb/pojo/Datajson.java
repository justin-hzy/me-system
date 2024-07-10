package com.me.modules.cbsb.pojo;

import com.me.common.pojo.Header;
import lombok.Data;

import java.util.List;

@Data
public class Datajson {

    private List<Data> data;

    private Header header;
}
