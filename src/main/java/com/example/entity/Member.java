package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

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
// @SequenceGenerator(initialValue = 1, name = "SEQ_MEMBER_NO", sequenceName =
// "SEQ_MEMBER_NO", allocationSize = 1)
@Table(name = "MEMBER")
public class Member {

    // @Column(name = "NO")
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
    // "SEQ_MEMBER_NO")
    // private long no;

    @Id
    @Column(name = "ID")
    private String Id;

    @Column(name = "PASSWORD")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Column(name = "NEWPW")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String newpw;

    @Column(name = "LOGIN")
    private String login = "TRAVEL";

    @Column(name = "NAME")
    private String name;

    @Column(name = "NICNAME")
    private String nicname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "ROLE")
    private String role = "USER";

    @Column(name = "STATE")
    private int state = 1;

    @Column(name = "TOKEN")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String token;

    // "yyyy-MM-dd HH:mm:ss"
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date regdate;

}
