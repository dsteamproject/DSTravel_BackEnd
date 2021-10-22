package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.City;
import com.example.entity.TravelDestination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TravelDestinationRepository extends JpaRepository<TravelDestination,Long>{
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SELECT * FROM TRAVELDESTINATION WHERE CITY=:#{#city.name}", 
        nativeQuery = true)
    public List<TravelDestination> querySelectTD(@Param("city") City city);
}
