package com.example.repository;

import com.example.entity.TD;
import com.example.entity.TDImg;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TDimgRepository extends JpaRepository<TDImg, Long> {

    @Query(value = "SELECT * FROM TDImg WHERE TD=:td ", nativeQuery = true)
    public TDImg querySelectByTD(@Param("td") TD td);
}
