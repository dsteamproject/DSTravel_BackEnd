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
@Table(name = "CAT2")
public class Cat2 {

    @Id
    @Column(name = "Code")
    private Integer Code;

    @Column(name = "NAME")
    private String Name;
}
