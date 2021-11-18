package com.example.repository;

import java.util.List;

import com.example.entity.City;
import com.example.entity.TD;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TDRepository extends JpaRepository<TD, Integer> {

        // 지역별, 가격별, 등급별 숙소 조회
        @Query(value = "SELECT * FROM TD WHERE PRICE>=:firstprice AND PRCIE<=:endprice AND CITY=:city AND RANK=:rank AND TYPE=32", nativeQuery = true)
        public List<TD> SelectHOTEL(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("city") Integer city, @Param("rank") int rank, Pageable pageable);

        @Query(value = "SELECT COUNT(*) FROM TD WHERE PRICE>=:firstprice AND PRCIE<=:endprice AND CITY=:city AND RANK=:rank AND TYPE=32", nativeQuery = true)
        public int COUNTSelectHOTEL(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("city") Integer city, @Param("rank") int rank);

        @Query(value = "SELECT * FROM TD WHERE CITY=:areaCode AND TYPE=:contentTypeId", nativeQuery = true)
        public List<TD> querySelectTD(@Param("areaCode") String areaCode, @Param("contentTypeId") String contentTypeId);

        // SELECT *,
        // (6371*acos(cos(radians(위도1))*cos(radians(위도2))*cos(radians(경도2)-radians(경도1))+sin(radians(위도1))*sin(radians(위도2))))
        // AS dist FROM 테이블명 HAVING dist <= 200/1000

        @Query(value = "SELECT * FROM TD WHERE CODE=:contentId", nativeQuery = true)
        public TD querySelectOneTD(@Param("contentId") String contentId);

        @Query(value = "SELECT * FROM TD WHERE Title LIKE '%' || :title || '%' AND TYPE=:contentTypeId AND CITY=:areaCode", nativeQuery = true)
        public List<TD> querySelectTD(@Param("title") String title, @Param("areaCode") String areaCode,
                        @Param("contentTypeId") String contentTypeId, Pageable pageable);

        @Query(value = "SELECT COUNT(*) FROM TD WHERE Title LIKE '%' || :title || '%' AND TYPE=:contentTypeId AND CITY=:areaCode", nativeQuery = true)
        public int CountSelectTD(@Param("title") String title, @Param("areaCode") String areaCode,
                        @Param("contentTypeId") String contentTypeId);

        @Query(value = "SELECT * FROM (SELECT *FROM TD WHERE CITY=:areaCode AND TYPE=:contentTypeId) WHERE (6371*acos(cos(radians(:ymap))*cos(radians(YLOCATION))*cos(radians(XLOCATION)-radians(:xmap))+sin(radians(:ymap))*sin(radians(YLOCATION)))) <= :distance AND XLocation !=:xmap", nativeQuery = true)
        public List<TD> querySelectdistanceTD(@Param("areaCode") String areaCode,
                        @Param("contentTypeId") String contentTypeId, @Param("xmap") Float xmap,
                        @Param("ymap") Float ymap, @Param("distance") double distance, Pageable pageable);

        @Query(value = "SELECT Count(*) FROM (SELECT *FROM TD WHERE CITY=:areaCode AND TYPE=:contentTypeId) WHERE (6371*acos(cos(radians(:ymap))*cos(radians(YLOCATION))*cos(radians(XLOCATION)-radians(:xmap))+sin(radians(:ymap))*sin(radians(YLOCATION)))) <= :distance AND XLocation !=:xmap", nativeQuery = true)
        public int CountSelectdistanceTD(@Param("areaCode") String areaCode,
                        @Param("contentTypeId") String contentTypeId, @Param("xmap") Float xmap,
                        @Param("ymap") Float ymap, @Param("distance") double distance);

}
