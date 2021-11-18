package com.example.mappers;

import java.util.List;

import com.example.dto.BoardDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

@Mapper
public interface BoardMapper {

    @Select({
            "SELECT * FROM BOARD WHERE TITLE LIKE '%' || #{keyword} || '%' AND CATEGORY=#{category} ORDER BY NO DESC" })
    public List<BoardDTO> selectBoardAdmin(@Param("keyword") String keyword, @Param("category") String category,
            Pageable pageable);

    @Select({ "SELECT COUNT(*) FROM BOARD WHERE TITLE LIKE '%' || #{keyword} || '%' AND CATEGORY=#{category}" })
    public int CountBoardAdmin(@Param("keyword") String keyword, @Param("category") String category);

}