package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.TD;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TDRepository extends JpaRepository<TD, Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SELECT * FROM TD WHERE CITY=#{#:TD.city} AND THEME=#{#:TD.theme}", nativeQuery = true)
    public List<TD> querySelectTD(@Param("TD") TD TD);
}
