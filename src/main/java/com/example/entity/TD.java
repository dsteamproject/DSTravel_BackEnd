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
    @Column(name = "NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TD_NO")
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

    @Column(name = "USERS")
    private String user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TDTYPE")
    private TDType tdtype;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY")
    private City city;

}
