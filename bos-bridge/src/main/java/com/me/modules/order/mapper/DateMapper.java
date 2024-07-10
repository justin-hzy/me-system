package com.me.modules.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface DateMapper {


    @Select("SELECT SYSDATE FROM DUAL")
    Date getCurrentDate();
}
