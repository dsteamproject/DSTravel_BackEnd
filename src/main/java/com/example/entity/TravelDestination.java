package com.example.entity;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@SequenceGenerator(initialValue = 20001, name = "SEQ_TD_NO", sequenceName = "SEQ_TD_NO", allocationSize = 1)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY")
    private City city;

}
