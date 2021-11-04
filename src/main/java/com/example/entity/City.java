package com.example.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @OneToMany(cascade = CascadeType.PERSIST)
    @Column(name = "TD")
    private List<TravelDestination> td = new ArrayList<>();

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "Hotel")
    // private Hotel hotel;

}
