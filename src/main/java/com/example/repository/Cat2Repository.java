package com.example.repository;

import com.example.entity.Cat2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Cat2Repository extends JpaRepository<Cat2, String> {

}
