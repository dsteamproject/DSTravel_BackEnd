package com.example.entity;

import java.util.Date;

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
@SequenceGenerator(initialValue = 1, name = "SEQ_REPLY_NO", sequenceName = "SEQ_REPLY_NO", allocationSize = 1)
@Table(name = "REPLY")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REPLY_NO")
    @Column(name = "NO")
    private long no;

    @Column(name = "REPLYCONTNENT")
    private String replycontent;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date regdate;

    @Column(name = "STATE")
    private int state = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BNO")
    private Board board;

}
