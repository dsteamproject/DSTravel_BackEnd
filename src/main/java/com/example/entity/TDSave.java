package com.example.entity;

import java.util.Map;

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
@SequenceGenerator(initialValue = 1, name = "SEQ_TDSAVE_NO", sequenceName = "SEQ_TDSAVE_NO", allocationSize = 1)
@Table(name = "TDSAVE")
public class TDSave {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TDSAVE_NO")
    @Column(name = "NO")
    private Long no;

    @Column(name = "TITLE")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member")
    private Member member;

    @Column(name = "TD")
    private String td;

    @Column(name = "STATE")
    private int state = 1;

}
