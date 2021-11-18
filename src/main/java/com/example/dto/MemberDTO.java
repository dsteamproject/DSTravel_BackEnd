package com.example.dto;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {

    private String Id;
    private String password;
    private String newpw;
    private String login = "TRAVEL";
    private String name;
    private String nicname;
    private String email;
    private String gender;
    private String role = "USER";
    private int state = 1;
    private String token;
    private Date regdate;
}
