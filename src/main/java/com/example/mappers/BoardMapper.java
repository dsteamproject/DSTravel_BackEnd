package com.example.mappers;

import java.util.List;

import com.example.dto.BoardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BoardMapper {

        @Select({ "<script>SELECT * FROM(SELECT NO,CATEGORY, CONTENT, GOOD, HIT, REGDATE, REPLY, STATE, TITLE, MEMBER, ROW_NUMBER() OVER (ORDER BY NO DESC) ROWN FROM BOARD"
                        + " <where> <if test='category !=null'> AND CATEGORY=#{category}</if>"
                        + " <if test='state !=null'> AND STATE=#{state}</if>"
                        + " AND TITLE LIKE '%' || #{keyword} || '%' ORDER BY NO DESC</where>) BOARD WHERE ROWN BETWEEN #{start} AND #{end}"
                        + "</script>" })
        public List<BoardDTO> selectBoardAdmin(@Param("keyword") String keyword, @Param("category") String category,
                        @Param("start") int start, @Param("end") int end, @Param("state") String state);

        @Select({ "<script>SELECT COUNT(*) FROM BOARD <where> <if test='category !=null'> AND CATEGORY=#{category}</if><if test='state !=null'> AND STATE=#{state}</if>"
                        + " AND TITLE LIKE '%' || #{keyword} || '%' </where></script>" })
        public int CountBoardAdmin(@Param("keyword") String keyword, @Param("category") String category,
                        @Param("state") String state);

        @Update({ "UPDATE BOARD SET HIT=0 ,CATEGORY='',CONTENT='',TITLE='' WHERE NO=#{no} AND STATE=0" })
        public int Admindelete(@Param("no") String no);

}