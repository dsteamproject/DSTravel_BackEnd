package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@SequenceGenerator(initialValue = 1, name = "SEQ_HOTEL_NO", sequenceName = "SEQ_HOTEL_NO", allocationSize = 1)
@Table(name = "HOTEL")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HOTEL_NO")
    @Column(name = "NO")
    private long hNo;

    @Column(name = "NAME")
    private String hName;

    @Column(name = "ADDRESS")
    private String hAddress;

    @Column(name = "XLOCATION")
    private String hxLocation;

    @Column(name = "YLOCATION")
    private String hyLocation;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date hregDate;

}
