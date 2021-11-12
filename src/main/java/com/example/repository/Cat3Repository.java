package com.example.repository;

import com.example.entity.Cat3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Cat3Repository extends JpaRepository<Cat3, String> {

}
