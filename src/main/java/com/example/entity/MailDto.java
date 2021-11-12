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
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "MESSAGE")
    private String message;
}
