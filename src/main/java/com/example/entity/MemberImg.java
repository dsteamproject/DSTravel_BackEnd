package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
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
@SequenceGenerator(initialValue = 1, name = "SEQ_MEMBERIMG_NO", sequenceName = "SEQ_MEMBERIMG_NO", allocationSize = 1)
@Table(name = "MEMBERIMG")
public class MemberImg {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEMBERIMG_NO")
    @Column(name = "NO")
    private Long no;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "IMAGENAME")
    private String imagename;

    @Column(name = "IMAGESIZE")
    private long imagesize;

    @Column(name = "IMAGETYPE")
    private String imagetype;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MID")
    private Member member;
}
