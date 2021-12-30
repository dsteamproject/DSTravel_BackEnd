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
@Table(name = "CITY")
public class City {

    @Id
    @Column
    private Integer code; // 지역코드 (한국관광공사 공공데이터에 등록된 지역코드)

    @Column
    private String name; // 지역명

}
