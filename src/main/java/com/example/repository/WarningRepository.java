package com.example.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import com.example.entity.Board;
import com.example.entity.Member;
import com.example.entity.Warning;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Long> {

    // ----------------------------board--------------------------
    @Query(value = "SELECT * FROM warning WHERE Board=:board AND Member=:member", nativeQuery = true)
    public Warning queryselectwarning(@Param("board") Board board, @Param("member") Member member);

    @Query(value = "SELECT * FROM warning WHERE Board=:board AND Member=:member", nativeQuery = true)
    public Optional<Warning> queryselectwarningstate(@Param("board") Board board, @Param("member") Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO warning VALUES (SEQ_GOOD_NO.NEXTVAL, :board , :member)", nativeQuery = true)
    public int queryinsertwarning(@Param("board") Board board, @Param("member") Member member);

    @Query(value = "SELECT COUNT(*) FROM warning WHERE Board=:board", nativeQuery = true)
    public int queryCountWarningByBoard(@Param("board") Board board);

    public int countByBoard_no(long no);
}
