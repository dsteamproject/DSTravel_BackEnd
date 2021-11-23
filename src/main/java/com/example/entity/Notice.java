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
@SequenceGenerator(initialValue = 1, name = "SEQ_NOTICE_NO", sequenceName = "SEQ_NOTICE_NO", allocationSize = 1)
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NOTICE_NO")
    @Column(name = "NO")
    private Long no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER")
    private Member member;

    @Column(name = "NOTICE")
    private String notice;
}
