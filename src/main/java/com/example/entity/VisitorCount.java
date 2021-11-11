package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@SequenceGenerator(initialValue = 1, name = "SEQ_VISITORCOUNT_NO", sequenceName = "SEQ_VISITORCOUNT_NO", allocationSize = 1)
public class VisitorCount {

    @Column(name = "VISIT_NO") // 컬럼명
    @Id // 기본키(중복X)
    // 여기 추가되는 정보는 위에 생성한 시퀀스 사용
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VISITORCOUNT_NO")
    private Long visit_no = 0L; // 글번호

    @Column(name = "VISIT_COUNT")
    private int visit_count = 0;

    @CreationTimestamp // 날짜는 자동으로 추가
    @Column(name = "VISIT_REGDATE")
    private Date visit_regdate;

}
