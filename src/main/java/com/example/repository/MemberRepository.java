package com.example.repository;

import java.util.Optional;

import com.example.entity.Member;

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

    // -------------회원정보 조회(state 1)-----------------------------
    @Query(value = "SELECT * FROM member WHERE id=:id AND state=1", nativeQuery = true)
    public Optional<Member> querySelectByid(@Param("id") String id);

    // -------------탈퇴 회원 조회(state 0)-----------------------
    @Query(value = "SELECT * FROM member WHERE id=:id AND STATE=0", nativeQuery = true)
    public Optional<Member> querySelectmember(@Param("id") String id);

}
