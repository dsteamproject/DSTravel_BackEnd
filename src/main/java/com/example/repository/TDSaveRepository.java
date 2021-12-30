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

    // 게시판 전체 조회
    // 수정 전
    // @Query(value = "SELECT * FROM TDSAVE WHERE TITLE LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO DESC", nativeQuery = true)
    // 수정 후 (쿼리에서 로직빼기 -> 주요 로직을 단위 테스트하기 어려움을 해소)
    // 참고했던 자료(https://www.youtube.com/watch?v=fnH_SR3n9Ew)
    @Query(value = "SELECT * FROM TDSAVE WHERE TITLE LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO DESC", nativeQuery = true)
    public List<TDSave> querySelectAllByTitleOrderByDesc(@Param("Keyword") String keyword,
                     Pageable pageable);

    @Query(value = "SELECT * FROM TDSAVE WHERE TDSAVE.member LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO DESC", nativeQuery = true)
    public List<TDSave> querySelectAllByWriterOrderByDesc(@Param("Keyword") String keyword,
                     Pageable pageable);

    @Query(value = "SELECT * FROM TDSAVE WHERE TITLE LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO ASC", nativeQuery = true)
    public List<TDSave> querySelectAllByTitleOrderByAsc(@Param("Keyword") String keyword,
                    Pageable pageable);

    @Query(value = "SELECT * FROM TDSAVE WHERE TDSAVE.member LIKE '%' || :Keyword || '%' AND STATE=2 ORDER BY NO ASC", nativeQuery = true)
    public List<TDSave> querySelectAllByWriterOrderByAsc(@Param("Keyword") String keyword,
                    Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM TDsave WHERE TITLE LIKE '%' || :Keyword || '%' AND STATE=2", nativeQuery = true)
    public int queryCountByTitle(@Param("Keyword") String keyword);

    @Query(value = "SELECT COUNT(*) FROM TDsave WHERE TDSAVE.member LIKE '%' || :Keyword || '%' AND STATE=2", nativeQuery = true)
    public int queryCountByWriter(@Param("Keyword") String keyword);

    @Query(value = "SELECT * FROM TDsave WHERE NO<:b AND STATE=2 ORDER BY NO DESC LIMIT 1", nativeQuery = true)
    public TDSave queryByCategoryTop1OrderByNoDesc(@Param("b") long no);

    @Query(value = "SELECT * FROM TDsave WHERE NO>:b AND STATE=2 ORDER BY NO ASC LIMIT 1", nativeQuery = true)
    public TDSave queryByCategoryTop1OrderByNoAsc(@Param("b") long no);

    @Query(value = "SELECT * FROM TDsave WHERE NO=:b AND STATE != 0", nativeQuery = true)
    public TDSave querySelectByIdstate12(@Param("b") long no);

}
