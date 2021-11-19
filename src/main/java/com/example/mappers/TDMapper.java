package com.example.mappers;

import java.util.List;

import com.example.entity.TD;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TDMapper {

    // 전체 여행지,숙소,검색 조회 + 임시여행지 조회
    @Select({ "SELECT * FROM TD WHERE Title LIKE '%' || :title || '%' AND TYPE=:contentTypeId AND CITY=:areaCode" })
    public List<TD> querySelectTDtem(@Param("title") String title, @Param("areaCode") String areaCode,
            @Param("contentTypeId") String contentTypeId, @Param("id") String id);

    // 전체 여행지,숙소,검색 조회 수 +임시여행지 조회 수
    @Select({
            "SELECT COUNT(*) FROM TD WHERE Title LIKE '%' || :title || '%' AND TYPE=:contentTypeId AND CITY=:areaCode" })
    public int CountSelectTDtem(@Param("title") String title, @Param("areaCode") String areaCode,
            @Param("contentTypeId") String contentTypeId, @Param("id") String id);

    // @Select({ "<script>SELECT * FROM(SELECT NO,CATEGORY, CONTENT, GOOD, HIT,
    // REGDATE, REPLY, STATE, TITLE, MEMBER, ROW_NUMBER() OVER (ORDER BY NO DESC)
    // ROWN FROM BOARD"
    // + " <where> <if test='category !=null'> AND CATEGORY=#{category}</if>"
    // + " <if test='state !=null'> AND STATE=#{state}</if>"
    // + " AND TITLE LIKE '%' || #{keyword} || '%' ORDER BY NO DESC</where>) BOARD
    // WHERE ROWN BETWEEN #{start} AND #{end}"
    // + "</script>" })
}
