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
@SequenceGenerator(initialValue = 1, name = "SEQ_WARNING_NO", sequenceName = "SEQ_WARNING_NO", allocationSize = 1)
@NoArgsConstructor
@Table(name = "WARNING")
public class Warning {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WARNING_NO")
    @Column(name = "NO")
    private Long no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOARD")
    private Board board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER")
    private Member member;

}
