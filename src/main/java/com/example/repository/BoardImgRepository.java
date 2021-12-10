package com.example.repository;

import com.example.entity.Board;
import com.example.entity.BoardImg;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {

    @Query(value = "SELECT * FROM BoardImg WHERE bno=:board", nativeQuery = true)
    public BoardImg querySelectByBoardno(@Param("board") Board board);

}
