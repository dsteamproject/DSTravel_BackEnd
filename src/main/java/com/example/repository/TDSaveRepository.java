package com.example.repository;

import java.util.List;

import com.example.entity.Member;
import com.example.entity.TDSave;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TDSaveRepository extends JpaRepository<TDSave, Long> {

    @Query(value = "SELECT * FROM TDSAVE WHERE MEMBER=:member AND STATE != 0", nativeQuery = true)
    public List<TDSave> selectMyTDsave(@Param("member") Member member);

    @Query(value = "SELECT * FROM TDSAVE WHERE TITLE LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO DESC", nativeQuery = true)
    public List<TDSave> querySelectAllByTitleOrderByDesc(@Param("Keyword") String keyword,
                     Pageable pageable);

    @Query(value = "SELECT * FROM TDSAVE WHERE MEMBER LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO DESC", nativeQuery = true)
    public List<TDSave> querySelectAllByWriterOrderByDesc(@Param("Keyword") String keyword,
                     Pageable pageable);

    @Query(value = "SELECT * FROM TDSAVE WHERE TITLE LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO ASC", nativeQuery = true)
    public List<TDSave> querySelectAllByTitleOrderByAsc(@Param("Keyword") String keyword,
                    Pageable pageable);

    @Query(value = "SELECT * FROM TDSAVE WHERE MEMBER LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO ASC", nativeQuery = true)
    public List<TDSave> querySelectAllByWriterOrderByAsc(@Param("Keyword") String keyword,
                    Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM TDsave WHERE TITLE LIKE '%' || :Keyword || '%' AND STATE=2", nativeQuery = true)
    public int queryCountByTitle(@Param("Keyword") String keyword);

    @Query(value = "SELECT COUNT(*) FROM TDsave WHERE MEMBER LIKE '%' || :Keyword || '%' AND STATE=2", nativeQuery = true)
    public int queryCountByWriter(@Param("Keyword") String keyword);

}
