package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.Board;
import com.example.entity.Good;
import com.example.entity.GoodProjection;
import com.example.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {

    @Query(value = "SELECT * FROM good WHERE Board=:board AND Member=:member", nativeQuery = true)
    public Good queryselectgood(@Param("board") Board board, @Param("member") Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO good VALUES (SEQ_GOOD_NO.NEXTVAL, :board , :member)", nativeQuery = true)
    public int queryinsertgood(@Param("board") Board board, @Param("member") Member member);

    @Query(value = "SELECT COUNT(*) FROM good WHERE Board=:board", nativeQuery = true)
    public int queryCountByBoard(@Param("board") Board board);

    public List<GoodProjection> findAllByMember(Member member);

}
