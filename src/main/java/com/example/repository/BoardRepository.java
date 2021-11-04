package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.Board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

        @Query(value = "SELECT COUNT(*) FROM Board WHERE TITLE LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1", nativeQuery = true)
        public int queryCountByTitle(@Param("Keyword") String keyword, @Param("a") String category1);

        @Query(value = "SELECT COUNT(*) FROM Board WHERE WRITER LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1", nativeQuery = true)
        public int queryCountByWriter(@Param("Keyword") String keyword, @Param("a") String category1);

        @Query(value = "SELECT * FROM Board WHERE CATEGORY=:a AND NO<:b AND STATE=1 ORDER BY NO DESC LIMIT 1;", nativeQuery = true)
        public Board queryByCategoryTop1OrderByNoDesc(@Param("a") String category1, @Param("b") long no);

        @Query(value = "SELECT * FROM Board WHERE CATEGORY=:a AND NO>:b AND STATE=1 ORDER BY NO ASC LIMIT 1;", nativeQuery = true)
        public Board queryByCategoryTop1OrderByNoAsc(@Param("a") String category1, @Param("b") long no);

        @Transactional
        @Query(value = "UPDATE Board board set board.state=0 where board.no=:a", nativeQuery = true)
        @Modifying(clearAutomatically = true)
        public int queryDelete(@Param("a") long no);

        @Query(value = "SELECT * FROM Board WHERE NO=:b AND STATE=1;", nativeQuery = true)
        public Board querySelectById(@Param("b") long no);

        // @Modifying(clearAutomatically = true)
        @Query(value = "SELECT * FROM Board WHERE TITLE LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO DESC", nativeQuery = true)
        public List<Board> querySelectAllByTitleOrderByDesc(
                        // @Param("b") String type,
                        @Param("Keyword") String keyword, @Param("a") String category1,
                        // @Param("c") String orderby,
                        Pageable pageable);

        @Query(value = "SELECT * FROM Board WHERE WRITER LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO DESC", nativeQuery = true)
        public List<Board> querySelectAllByWriterOrderByDesc(@Param("Keyword") String keyword,
                        @Param("a") String category1, Pageable pageable);

        @Query(value = "SELECT * FROM Board WHERE TITLE LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO ASC", nativeQuery = true)
        public List<Board> querySelectAllByTitleOrderByAsc(@Param("Keyword") String keyword,
                        @Param("a") String category1, Pageable pageable);

        @Query(value = "SELECT * FROM Board WHERE WRITER LIKE '%' || :Keyword || '%' AND CATEGORY=:a AND STATE=1 ORDER BY NO ASC", nativeQuery = true)
        public List<Board> querySelectAllByWriterOrderByAsc(@Param("Keyword") String keyword,
                        @Param("a") String category1, Pageable pageable);

        @Query(value = "SELECT * FROM Board WHERE WRITER=:writer ORDER BY NO DESC", nativeQuery = true)
        public List<Board> querySelectByWriter(@Param("writer") String writer);
}
