package com.example.repository;

import com.example.entity.TravelDestination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<TravelDestination, Long> {

}
