package com.example.repository;

import java.util.List;

import com.example.entity.Member;
import com.example.entity.TDSave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TDSaveRepository extends JpaRepository<TDSave, Long> {

    @Query(value = "SELECT * FROM TDSAVE WHERE MEMBER=:member", nativeQuery = true)
    public List<TDSave> selectMyTDsave(@Param("member") Member member);

}
