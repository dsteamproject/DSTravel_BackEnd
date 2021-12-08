package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

@Table(name = "MEMBER")
public class Member {

    @Id
    @Column(name = "ID")
    private String Id = null;

    @Column(name = "PASSWORD")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password = null;

    @Column(name = "NEWPW")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String newpw = null;

    @Column(name = "LOGIN")
    private String login = "TRAVEL";

    @Column(name = "NAME")
    private String name = null;

    @Column(name = "NICNAME")
    private String nicname = null;

    @Column(name = "EMAIL")
    private String email = null;

    @Column(name = "GENDER")
    private String gender = null;

    @Column(name = "ROLE")
    private String role = "USER";

    @Column(name = "STATE")
    private int state = 1;

    @Column(name = "TOKEN")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String token = null;

    // "yyyy-MM-dd HH:mm:ss"
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date regdate = null;

}
