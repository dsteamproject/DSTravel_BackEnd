package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@SequenceGenerator(initialValue = 1, name = "SEQ_TODATVISITCOUNT_NO", sequenceName = "SEQ_TODYVISITCOUNT_NO", allocationSize = 1)
public class TodayVisitCount {
	@Column(name = "NO")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TODATVISITCOUNT_NO")
	private Long no = 0L; // 글번호

	@Column(name = "TODAY_VISIT_COUNT")
	private int today_visit_count = 0;

	// @CreationTimestamp
	@Column(name = "TODAY_VISIT_REGDATE")
	private Date today_visit_regdate;

}
