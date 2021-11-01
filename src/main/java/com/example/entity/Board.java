package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@SequenceGenerator(initialValue = 1, name = "SEQ_BOARD_NO", sequenceName = "SEQ_BOARD_NO", allocationSize = 1)
@NoArgsConstructor
@Table(name = "BOARD")
public class Board {

    @Column(name = "NO") // 컬럼명
    @Id // 기본키(중복X)
    // 여기 추가되는 정보는 위에 생성한 시퀀스 사용
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD_NO")
    private Long no = 0L; // 글번호

    @Column(name = "TITLE")
    private String title = null; // 글제목

    @Lob
    @Column(name = "CONTENT")
    private String content = null; // 내용

    @Column(name = "WRITER")
    private String writer = null; // 작성자

    @Column(name = "HIT")
    private int hit = 1; // 조회수

    @CreationTimestamp // 날짜는 자동으로 추가
    @Column(updatable = false, name = "REGDATE")
    private Date regdate = null; // 날짜

    // String => varchar2
    // long => number
    // byte[] => blob
    // 오라클에서 byte[]을 저장하는 타입을 BLOB CLOB
    // @Lob
    // @Column(name = "IMAGE")
    // private byte[] image = null; // 이미지데이터

    // @Column(name = "IMAGENAME")
    // private String imagename = null; // 파일명

    // @Column(name = "IMAGESIZE", nullable = false, columnDefinition = "long
    // default 0")
    // private long imagesize = 0L; // 파일 사이즈

    // @Column(name = "IMAGETYPE")
    // private String imagetype = null; // 파일 종류
}
