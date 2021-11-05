package com.example.repository;

import javax.transaction.Transactional;

import com.example.entity.Member;
import com.example.entity.MemberImg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {

    @Query(value = "SELECT * FROM memberImg WHERE mid=:member ", nativeQuery = true)
    public MemberImg querySelectByMemberId(@Param("member") Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE memberImg Set image=:#{#file.image}, imagename=:#{#file.imagename}, imagesize=:#{#file.imagesize}, imagetype=:#{#file.imagetype} WHERE mid=:member ", nativeQuery = true)
    public int queryupdate(@Param("file") MemberImg memberimg, @Param("member") Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO memberImg VALUES (SEQ_MEMBERIMG_NO.NEXTVAL, :#{#file.image}, :#{#file.imagename}, :#{#file.imagesize}, :#{#file.imagetype}, :#{#file.member}) ", nativeQuery = true)
    public int queryinsert(@Param("file") MemberImg memberimg);

}
