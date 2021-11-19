package com.example.repository;

import com.example.entity.TDtem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TDtemRepository extends JpaRepository<TDtem, Long> {

}
