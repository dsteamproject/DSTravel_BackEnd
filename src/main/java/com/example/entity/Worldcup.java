package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@SequenceGenerator(initialValue = 1, name = "SEQ_WORLDCUP_NO", sequenceName = "SEQ_WORLDCUP_NO", allocationSize = 1)
@NoArgsConstructor
@Table(name = "WORLDCUP")
public class Worldcup {
    
    @Column(name = "NO") // 컬럼명
    @Id // 기본키(중복X)
    // 여기 추가되는 정보는 위에 생성한 시퀀스 사용
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORLDCUP_NO")
    private Long no = 0L; // 글번호

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TD")
    private TD td;
}
