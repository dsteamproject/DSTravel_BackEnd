package com.example.dto;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long no = 0L; // 글번호
    private String title = null; // 글제목
    private String category = null;
    private String content = null; // 내용
    private int hit = 1; // 조회수
    private int good = 0; // 좋아요수
    private int reply = 0; // 리플수
    private Date regdate = null; // 날짜
    private int state = 1;
    private MemberDTO member;

}
