package com.example.repository;


import com.example.entity.TDType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<TDType, Integer> {

}
