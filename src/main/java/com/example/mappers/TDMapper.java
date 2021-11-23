package com.example.mappers;

import java.util.List;

import com.example.dto.BoardDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TDMapper {

        // ---------------------여행지 임시저장 ADMIN List----------------------------
        // @Query(value = "SELECT * FROM TD WHERE USER IS NOT NULL AND USER !='ADMIN'
        // AND STATE=:state", nativeQuery = true)
        // public List<TD> selectAdminTDtem(@Param("state") int state);

        // private Integer no;
        // private Integer code;
        // private String title;
        // private String addr;
        // private Float xlocaion;
        // private Float ylocation;
        // private String tel;
        // private String firstimage;
        // private int good = 0; // 좋아요수
        // private int price;
        // private int rank = 0;
        // private Integer type;
        // private Integer cat1;
        // private Integer cat2;
        // private Integer cat3;
        // private Integer city;
        // private Integer sigungu;

}
