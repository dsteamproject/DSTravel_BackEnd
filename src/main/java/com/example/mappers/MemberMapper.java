package com.example.mappers;

import java.util.List;

import com.example.dto.MemberDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {

	@Select({
			"<script>SELECT * FROM(SELECT ID,EMAIL,GENDER,LOGIN,NAME,REGDATE,ROLE,STATE,ROW_NUMBER() OVER (ORDER BY ${orderbytype} ${orderby}, ID ${orderby}) ROWN FROM MEMBER"
					+ " <where><if test='state !=null'> AND STATE=#{state}</if>"
					+ " AND ${keywordtype} LIKE '%' || #{keyword} || '%' </where>) MEMBER WHERE ROWN BETWEEN #{start} AND #{end} ORDER BY ROWN ASC"
					+ " </script>" })
	public List<MemberDTO> ListMember(@Param("keywordtype") String keywordtype, @Param("keyword") String keyword,
			@Param("start") int start, @Param("end") int end, @Param("orderbytype") String orderbytype,
			@Param("orderby") String orderby, @Param("state") String state);

	@Select({ "<script>SELECT COUNT(*) FROM MEMBER <where><if test='state!=null'> AND STATE=#{state}</if>"
			+ " AND ${keywordtype} LIKE '%' || #{keyword} || '%' </where></script>" })
	public int ListMemberTotalCount(@Param("keywordtype") String keywordtype, @Param("keyword") String keyword,
			@Param("state") String state);

}
