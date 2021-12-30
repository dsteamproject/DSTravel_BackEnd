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

    @Column 
    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD_NO") // 시퀀스 사용
    private Long no = 0L; // 글번호

    @Column
    private String title = null; // 글제목

    @Column
    private String category = null; // 카테고리

    @Lob
    @Column
    private String content = null; // 내용(Front에서 html형식으로 내용저장하기로 함)

    @Column
    private int hit = 1; // 조회수

    @Column
    private int good = 0; // 좋아요수

    @Column
    private int reply = 0; // 리플수

    @Column
    private int warning = 0; // 신고수

    @CreationTimestamp 
    @Column(updatable = false) // 최초등록일자는 수정불가
    private Date regdate = null; // 등록일자

    @Column
    private int state = 1; // 상태(1:등록된 게시물 0:삭제된 게시물)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Member member;

}
