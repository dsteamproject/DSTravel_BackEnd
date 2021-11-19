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
public class GoodDTO {

    private Long no;
    private Long board;
    private String member;
    private Integer td;

}
