package com.example.repository;

import com.example.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByEmail(String email);

    Member findByName(String name);

    @Query(value = "SELECT * FROM member WHERE id=:id state=1", nativeQuery = true)
    public Member querySelectByid(@Param("id") String id);

}
