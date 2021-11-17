package com.example.repository;

import com.example.entity.Member;
import com.example.entity.MemberProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByEmail(String email);

    Member findByName(String name);

    // 아이디로 회원정보 조회
    // @Query(value = "SELECT * FROM member WHERE id=:id state=1", nativeQuery =
    // true)
    // public Member querySelectByid(@Param("id") String id);

    @Query(value = "SELECT * FROM member WHERE id=:id state=1", nativeQuery = true)
    public Member querySelectByid(@Param("id") String id);

    @Query(value = "SELECT * FROM member WHERE id=:id", nativeQuery = true)
    public MemberProjection querySelectmemberprojection(@Param("id") String id);
}
