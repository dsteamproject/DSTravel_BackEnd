package com.example.repository;

import com.example.entity.Cat1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Cat1Repository extends JpaRepository<Cat1, String> {

}
