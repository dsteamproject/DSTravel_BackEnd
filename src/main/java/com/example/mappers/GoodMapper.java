package com.example.mappers;

import java.util.List;

import com.example.dto.GoodDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GoodMapper {

    @Select({ "SELECT NO,BOARD,MEMBER FROM GOOD WHERE MEMBER=#{member} AND BOARD IS NOT NULL" })
    public List<GoodDTO> selectGoodBoard(@Param("member") String member);

    @Select({ "SELECT NO,TD,MEMBER FROM GOOD WHERE MEMBER=#{member} AND TD IS NOT NULL" })
    public List<GoodDTO> selectGoodTD(@Param("member") String member);

}
