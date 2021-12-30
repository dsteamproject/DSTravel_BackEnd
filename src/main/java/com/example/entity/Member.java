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
    @Column
    private String Id = null;               // 아이디

    @Column
    @JsonProperty(access = Access.WRITE_ONLY)   
    private String password = null;         // 암호(조회불가)

    @Column
    @JsonProperty(access = Access.WRITE_ONLY)
    private String newpw = null;            // 임시 및 신규암호(조회불가)

    @Column
    private String login = "TRAVEL";        // 로그인 방법(Travel : 홈페이지 로그인, SNS : SNS계정 로그인)

    @Column
    private String name = null;             // 이름

    @Column
    private String nicname = null;          // 별명

    @Column
    private String email = null;            // 이메일

    @Column
    private String gender = null;           // 성별

    @Column
    private String role = "USER";           // 권한

    @Column
    private int state = 1;                  // 상태(0 : 삭제된 계정, 1 : 등록된 계정)

    @Column
    @JsonProperty(access = Access.WRITE_ONLY)
    private String token = null;            // JWT token

    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date regdate = null;            // 등록일자(수정불가)

}
