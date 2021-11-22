package com.example.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.example.entity.Board;
import com.example.entity.Good;
import com.example.entity.Member;
import com.example.entity.TD;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {

    // ----------------------------board--------------------------
    @Query(value = "SELECT * FROM good WHERE Board=:board AND Member=:member", nativeQuery = true)
    public Good queryselectgood(@Param("board") Board board, @Param("member") Member member);

    @Query(value = "SELECT * FROM good WHERE Board=:board AND Member=:member", nativeQuery = true)
    public Optional<Good> queryselectgoodstate(@Param("board") Board board, @Param("member") Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO good VALUES (SEQ_GOOD_NO.NEXTVAL, :board , :member)", nativeQuery = true)
    public int queryinsertgood(@Param("board") Board board, @Param("member") Member member);

    @Query(value = "SELECT COUNT(*) FROM good WHERE Board=:board", nativeQuery = true)
    public int queryCountByBoard(@Param("board") Board board);

    // --------------------------- TD-------------------------------
    @Query(value = "SELECT * FROM good WHERE TD=:td AND Member=:member", nativeQuery = true)
    public Good queryselectgoodTD(@Param("td") TD td, @Param("member") Member member);

    @Query(value = "SELECT * FROM good WHERE TD=:td AND Member=:member", nativeQuery = true)
    public Optional<Good> queryselectgoodstateTD(@Param("td") TD td, @Param("member") Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO good VALUES (SEQ_GOOD_NO.NEXTVAL, :td , :member)", nativeQuery = true)
    public int queryinsertgoodTD(@Param("td") TD td, @Param("member") Member member);

    @Query(value = "SELECT COUNT(*) FROM good WHERE TD=:td", nativeQuery = true)
    public int queryCountByTD(@Param("td") TD td);

    // -----------------------------------------------------------------

    public List<Good> findAllByBoard(Board board);

    public int countByBoard_no(long no);

    public int countByTd_no(Integer no);

    @Query(value = "SELECT COUNT(*), good.board FROM good GROUP BY board", nativeQuery = true)
    public List<Good> queryCountAllByBoard();
}
