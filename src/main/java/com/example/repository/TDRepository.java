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

        // 가격별 전체 숙소 조회
        @Query(value = "SELECT * FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND TYPE=32", nativeQuery = true)
        public List<TD> SelectHOTEL(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        Pageable pageable);

        @Query(value = "SELECT COUNT(*) FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND TYPE=32", nativeQuery = true)
        public int COUNTSelectHOTEL(@Param("firstprice") int firstprice, @Param("endprice") int endprice);

        // 가격별, 지역별 숙소 조회
        @Query(value = "SELECT * FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND CITY=:city AND TYPE=32", nativeQuery = true)
        public List<TD> SelectHOTELBYCITY(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("city") Integer city, Pageable pageable);

        @Query(value = "SELECT COUNT(*) FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND CITY=:city AND TYPE=32", nativeQuery = true)
        public int COUNTSelectHOTELBYCITY(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("city") Integer city);

        // 가격별, 등급별 숙소 조회
        @Query(value = "SELECT * FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND RANK=:rank AND TYPE=32", nativeQuery = true)
        public List<TD> SelectHOTELBYRANK(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("rank") Integer rank, Pageable pageable);

        @Query(value = "SELECT COUNT(*) FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND RANK=:rank AND TYPE=32", nativeQuery = true)
        public int COUNTSelectHOTELBYRANK(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("rank") Integer rank);

        // 지역별, 가격별, 등급별 숙소 조회
        @Query(value = "SELECT * FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND CITY=:city AND RANK=:rank AND TYPE=32", nativeQuery = true)
        public List<TD> SelectHOTELBYCITYANDRANK(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("city") Integer city, @Param("rank") Integer rank, Pageable pageable);

        @Query(value = "SELECT COUNT(*) FROM TD WHERE PRICE>=:firstprice AND PRICE<=:endprice AND CITY=:city AND RANK=:rank AND TYPE=32", nativeQuery = true)
        public int COUNTSelectHOTELBYCITYANDRANK(@Param("firstprice") int firstprice, @Param("endprice") int endprice,
                        @Param("city") Integer city, @Param("rank") Integer rank);

        @Query(value = "SELECT * FROM TD WHERE CITY=:areaCode AND TYPE=:contentTypeId", nativeQuery = true)
        public List<TD> querySelectTD(@Param("areaCode") String areaCode, @Param("contentTypeId") String contentTypeId);

        // SELECT *,
        // (6371*acos(cos(radians(위도1))*cos(radians(위도2))*cos(radians(경도2)-radians(경도1))+sin(radians(위도1))*sin(radians(위도2))))
        // AS dist FROM 테이블명 HAVING dist <= 200/1000

        @Query(value = "SELECT * FROM TD WHERE CODE=:contentId", nativeQuery = true)
        public TD querySelectOneTD(@Param("contentId") String contentId);

        // 전체 여행지,숙소,검색 조회
        @Query(value = "SELECT * FROM TD WHERE Title LIKE '%' || :title || '%' AND TYPE=:contentTypeId AND CITY=:areaCode AND STATE=1", nativeQuery = true)
        public List<TD> querySelectTD(@Param("title") String title, @Param("areaCode") String areaCode,
                        @Param("contentTypeId") String contentTypeId, Pageable pageable);

        // 전체 여행지,숙소,검색 조회 수
        @Query(value = "SELECT COUNT(*) FROM TD WHERE Title LIKE '%' || :title || '%' AND TYPE=:contentTypeId AND CITY=:areaCode AND STATE=1", nativeQuery = true)
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
