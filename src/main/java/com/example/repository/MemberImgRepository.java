package com.example.repository;

import com.example.entity.MemberImg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {

}
