package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.Hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SELECT * FROM Hotel WHERE CITY=#{#:Hotel.city}", nativeQuery = true)
    public List<Hotel> querySelectHotel(@Param("Hotel") Hotel Hotel);
}
