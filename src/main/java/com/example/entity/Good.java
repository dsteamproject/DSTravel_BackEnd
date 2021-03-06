package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@SequenceGenerator(initialValue = 1, name = "SEQ_GOOD_NO", sequenceName = "SEQ_GOOD_NO", allocationSize = 1)
@NoArgsConstructor
@Table(name = "GOOD")
public class Good {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GOOD_NO")
    @Column
    private Long no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOARD")
    private Board board;    // 게시글 번호

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER")
    private Member member;  // 회원정보 ID

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TD")
    private TD td;          // 여행지 번호

}
