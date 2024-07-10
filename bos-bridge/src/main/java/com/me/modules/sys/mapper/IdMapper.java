package com.me.modules.sys.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface IdMapper {


    @Select("select get_sequences(#{p_tablename}) from dual")
    Integer get_sequences(@Param("p_tablename")String p_tablename);
    @Select("select get_sequenceno(#{p_seqname},#{p_clientid}) from dual")
    String get_sequenceno(@Param("p_seqname")String p_seqname,@Param("p_clientid")Integer p_clientid);

}
