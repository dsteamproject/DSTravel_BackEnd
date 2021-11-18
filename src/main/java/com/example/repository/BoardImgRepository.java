package com.example.repository;

import java.util.List;

import com.example.entity.Board;
import com.example.entity.BoardImg;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {

    private BoardImg findByBno(Board board);

}
