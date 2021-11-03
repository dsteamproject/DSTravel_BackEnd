package com.example.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

// import javax.transaction.Transactional;

import com.example.entity.Board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
        // 제목에 단어가 포함된 전체 개수
        long countByTitleContaining(String title);

        long countByWriterContaining(String wirter);

        // 이전글 현재글이 20번이면 작은것중에서 가장큰것 1개
        Optional<Board> findTop1ByNoLessThanOrderByNoDesc(long no);

        // 다음글 현재글이 20번이면 큰것중에서 가장 작은것 1개
        Optional<Board> findTop1ByNoGreaterThanOrderByNoAsc(Long no);

        @Transactional
        @Query(value = "UPDATE Board board set board.state=0 where board.no=:a", nativeQuery = true)
        @Modifying(clearAutomatically = true)
        public int queryDelete(@Param("a") long no);

        // @Modifying(clearAutomatically = true)
        @Query(value = "SELECT * FROM Board WHERE TITLE LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO DESC", nativeQuery = true)
        public List<Board> querySelectAllByTitleOrderByDesc(
                        // @Param("b") String type,
                        @Param("Keyword") String keyword, @Param("a") String category1,
                        // @Param("c") String orderby,
                        Pageable pageable);

        @Query(value = "SELECT * FROM Board WHERE WRITER LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO DESC", nativeQuery = true)
        public List<Board> querySelectAllByWriterOrderByDesc(
                        // @Param("b") String type,
                        @Param("Keyword") String keyword, @Param("a") String category1,
                        // @Param("c") String orderby,
                        Pageable pageable);

        @Query(value = "SELECT * FROM Board WHERE TITLE LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO ASC", nativeQuery = true)
        public List<Board> querySelectAllByTitleOrderByAsc(
                        // @Param("b") String type,
                        @Param("Keyword") String keyword, @Param("a") String category1,
                        // @Param("c") String orderby,
                        Pageable pageable);

        @Query(value = "SELECT * FROM Board WHERE WRITER LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO ASC", nativeQuery = true)
        public List<Board> querySelectAllByWriterOrderByAsc(
                        // @Param("b") String type,
                        @Param("Keyword") String keyword, @Param("a") String category1,
                        // @Param("c") String orderby,
                        Pageable pageable);

}
