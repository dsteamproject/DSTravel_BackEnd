package com.example.repository;

import java.util.List;
import java.util.Optional;

import com.example.entity.City;
import com.example.entity.Sigungu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SigunguRepository extends JpaRepository<Sigungu, Integer> {

    @Query(value = "SELECT * FROM Sigungu WHERE Code=:code AND City=:city", nativeQuery = true)
    public Optional<Sigungu> queryfindByCode(@Param("code") Integer code, @Param("city") City city);

    @Query(value = "SELECT * FROM Sigungu WHERE City=:city", nativeQuery = true)
    public List<Sigungu> queryfindByCode(@Param("city") City city);

}
