package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@SequenceGenerator(initialValue = 40001, name = "SEQ_TD_NO", sequenceName = "SEQ_TD_NO", allocationSize = 1)
@Table(name = "TRAVELDESTINATION")
public class TravelDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TD_NO")
    @Column(name = "NO")
    private Long tNo;

    @Column(name = "TITLE")
    private String tTitle;

    @Column(name = "ADDRESS")
    private String tAddress;

    @Column(name = "XLOCATION")
    private String txLocation;

    @Column(name = "YLOCATION")
    private String tyLocation;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date tregDate;

    // FetchType 결정
    // TravelDestination 조회시
    // 대부분 CITY가 같이 조회된다면 EAGER(즉시로딩)으로 처리하고,
    // 같이 조회되지 않는 경우가 많다면 LAZY(지연로딩)으로 처리한다.
    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "CITY")
    // private City city;

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
