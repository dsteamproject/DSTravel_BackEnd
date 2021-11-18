package com.example.repository;

import java.util.List;

import com.example.entity.Board;
import com.example.entity.BoardImg;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {

    @Query(value = "SELECT * FROM BOARDIMG WHERE BNO=:board;", nativeQuery = true)
    public BoardImg findByBNO(@Param("board") Board board);

}
