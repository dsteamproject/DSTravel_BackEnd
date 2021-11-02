package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@SequenceGenerator(initialValue = 1, name = "SEQ_REPLY_NO", sequenceName = "SEQ_REPLY_NO", allocationSize = 1)
@Table(name = "REPLY")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REPLY_NO")
    @Column(name = "NO")
    private long no;

    @Column(name = "REPLY")
    private String reply;

    @Column(name = "REGDATE")
    private Date regdate;

    @JoinColumn(name = "BNO")
    @ManyToOne
    private Board board;

}
