package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@SequenceGenerator(initialValue = 1, name = "SEQ_VISITOR_NO", sequenceName = "SEQ_VISITOR_NO", allocationSize = 1)
@Table(name = "VISITOR")
public class Visitor {

    @Column(name = "VISIT_NO") // 컬럼명
    @Id // 기본키(중복X)
    // 여기 추가되는 정보는 위에 생성한 시퀀스 사용
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VISITOR_NO")
    private Long visit_no = 0L; // 글번호

    @Column(name = "VISIT_IP")
    private String visit_ip = null;
    @Column(name = "VISIT_TIME")
    private int visit_time;
    @Column(name = "VISIT_REFER")
    private int visit_refer;
    @Column(name = "VISIT_AGENT")
    private int visit_agent;

}
