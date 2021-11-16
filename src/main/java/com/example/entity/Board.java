package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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

    @Column(name = "CATEGORY")
    private String category = null;

    @Lob
    @Column(name = "CONTENT")
    private String content = null; // 내용

    @Column(name = "HIT")
    private int hit = 1; // 조회수

    @Column(name = "GOOD")
    private int good = 0; // 좋아요수

    @Column(name = "reply")
    private int reply = 0; // 리플수

    @CreationTimestamp // 날짜는 자동으로 추가
    @Column(updatable = false, name = "REGDATE")
    private Date regdate = null; // 날짜

    @Column(name = "STATE")
    private int state = 1;

    // @Lob
    // @Column(name = "IMAGE")
    // private byte[] image;

    // @Column(name = "IMAGENAME")
    // private String imagename;

    // @Column(name = "IMAGESIZE")
    // private long imagesize;

    // @Column(name = "IMAGETYPE")
    // private String imagetype;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER")
    private Member member;

}
