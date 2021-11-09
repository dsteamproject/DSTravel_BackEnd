package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
@Table(name = "MEMBER")
public class Member {
    @Id
    @Column(name = "ID", length = 100)
    private String Id;

    @Column(name = "PASSWORD")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Column(name = "NEWPW")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String newpw;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NICNAME")
    private String nicname;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "STATE")
    private int state = 1;

    @Column(name = "TOKEN")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String token;

    // "yyyy-MM-dd HH:mm:ss"
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "REGDATE", updatable = false)
    private Date regdate;

}
