package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "MAILDTO")
public class MailDto {
    @Id
    @Column
    private String address;     // 이메일 주소

    @Column(name = "TITLE")
    private String title;       // 이메일 제목

    @Column(name = "MESSAGE")
    private String message;     // 이메일 내용
}
