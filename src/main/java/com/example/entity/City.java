package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
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
@SequenceGenerator(initialValue = 1, name = "SEQ_CITY_NO", sequenceName = "SEQ_CITY_NO", allocationSize = 1)
@Table(name = "CITY")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CITY_NO")
    @Column(name = "NO")
    private long cNo;

    @Column(name = "NAME")
    private String cName;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "IMAGENAME")
    private String imagename;

    @Column(name = "IMAGESIZE")
    private long imagesize;

    @Column(name = "IMAGETYPE")
    private String imagetype;

}
