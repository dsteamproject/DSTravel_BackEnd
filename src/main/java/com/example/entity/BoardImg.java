package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@SequenceGenerator(initialValue = 1, name = "SEQ_BOARDIMG_NO", sequenceName = "SEQ_BOARDIMG_NO", allocationSize = 1)
@Table(name = "BOARDIMG")
public class BoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARDIMG_NO")
    @Column
    private Long no; // 번호

    @Lob
    @Column
    private byte[] image; // 이미지 데이터

    @Column
    private String imagename; // 이미지 명

    @Column
    private long imagesize; // 이미지 크기

    @Column
    private String imagetype; // 이미지 타입

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOARD_NO") // Column명 : BOARD_NO
    private Board board;
}
