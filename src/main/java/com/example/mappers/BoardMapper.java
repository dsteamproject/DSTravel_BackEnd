package com.example.mappers;

import java.util.List;

import com.example.dto.BoardDTO;
import com.example.entity.Board;
import com.example.entity.Member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

@Mapper
public interface BoardMapper {

        // @Select("<script>SELECT ID, COMPANY, SYSTEM, SYSTEM_INSTANCE, TYPE, VALUE " +
        // "FROM OBJECT_REFERENCE "
        // + "<where>"
        // + "<if test='company != null'>AND COMPANY IN(<foreach item='item'
        // collection='company' separator=',' >#{item}</foreach>)</if> "
        // + "<if test='system != null'>AND SYSTEM IN(<foreach item='item'
        // collection='system' separator=',' >#{item}</foreach>)</if> "
        // + "<if test='systemInstance != null'>AND SYSTEM_INSTANCE IN(<foreach
        // item='item' collection='systemInstance' separator=','
        // >#{item}</foreach>)</if> "
        // + "<if test='type != null'>AND TYPE IN(<foreach item='item' collection='type'
        // separator=',' >#{item}</foreach>)</if> "
        // + "<if test='value != null'>AND VALUE IN(<foreach item='item'
        // collection='value' separator=',' >#{item}</foreach>)</if> "
        // + "</where>" + "</script>")

        @Select({ "<script>SELECT * FROM BOARD <where> <if test='category !=null'> AND CATEGORY=#{category}</if>"
                        + " AND TITLE LIKE '%' || #{keyword} || '%' ORDER BY NO DESC</where></script>" })
        // @Results({ @Result(property = "", column = "MEMBER", javaType = Member.class)
        // })
        public List<BoardDTO> selectBoardAdmin(@Param("keyword") String keyword, @Param("category") String category,
                        Pageable pageable);

        @Select({ "SELECT COUNT(*) FROM BOARD WHERE TITLE LIKE '%' || #{keyword} || '%' AND CATEGORY=#{category}" })
        public int CountBoardAdmin(@Param("keyword") String keyword, @Param("category") String category);

}