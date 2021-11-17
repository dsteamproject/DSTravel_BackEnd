package com.example.repository;

import com.example.entity.TodayVisitCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodayVisitCountRepository extends JpaRepository<TodayVisitCount, Long> {

}
