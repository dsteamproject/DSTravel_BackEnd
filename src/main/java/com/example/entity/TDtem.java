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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@SequenceGenerator(initialValue = 1, name = "SEQ_TDtem_NO", sequenceName = "SEQ_TDtem_NO", allocationSize = 1)
public class TDtem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TD_NO")
    @Column(name = "NO")
    private Long no;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "ADDR")
    private String addr;

    @Column(name = "XLOCATION")
    private Float xlocation;

    @Column(name = "YLOCATION")
    private Float ylocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private Type type;

}
