package com.example.dto;

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
public class TDDTO {

    private Integer no;
    private Integer code;
    private String title;
    private String addr;
    private Float xlocaion;
    private Float ylocation;
    private String tel;
    private String firstimage;
    private int good = 0; // 좋아요수
    private int price;
    private int rank = 0;
    private Integer type;
    private Integer cat1;
    private Integer cat2;
    private Integer cat3;
    private Integer city;
    private Integer sigungu;

}
