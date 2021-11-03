package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.Reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SELECT * FROM Reply WHERE BNO=:NO", nativeQuery = true)
    public List<Reply> querySelectReply(@Param("NO") Long no);

    @Transactional
    // @Modifying(clearAutomatically = true)
    @Query(value = "SELECT COUNT(*) FROM Reply WHERE BNO=:NO AND State=1", nativeQuery = true)
    public int queryCountSelectReply(@Param("NO") Long no);

    // List<Reply> findByBno_No(long no);
}
