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
@NoArgsConstructor
@SequenceGenerator(initialValue = 1, name = "SEQ_TD_NO", sequenceName = "SEQ_TD_NO", allocationSize = 1)
@Table(name = "TD")
public class TD {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TD_NO")
    @Column(name = "NO")
    private Integer no;

    @Column(name = "CODE")
    private Integer code;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "ADDR")
    private String addr;

    @Column(name = "XLOCATION")
    private Float xlocation;

    @Column(name = "YLOCATION")
    private Float ylocation;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "FIRSTIMAGE")
    private String firstimage;

    @Column(name = "GOOD")
    private int good = 0; // 좋아요수

    @Column(name = "PRICE")
    private int price;

    @Column(name = "RANK")
    private int rank = 0;

    @Column(name = "STATE")
    private int state = 1;

    @Column(name = "USER")
    private String user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private Type type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAT1")
    private Cat1 cat1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAT2")
    private Cat2 cat2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAT3")
    private Cat3 cat3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY")
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SIGUNGU")
    private Sigungu sigungu;

    // FetchType 결정
    // TravelDestination 조회시
    // 대부분 CITY가 같이 조회된다면 EAGER(즉시로딩)으로 처리하고,
    // 같이 조회되지 않는 경우가 많다면 LAZY(지연로딩)으로 처리한다.

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "THEME")
    // private Theme theme;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "SUGMONTH")
    // private SugMonth Mon;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "TDIMG")
    // private TDimg timg;

}
