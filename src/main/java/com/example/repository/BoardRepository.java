package com.example.repository;

import java.util.List;
import java.util.Optional;

import com.example.entity.Board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 한개조회 글번호가 일치하는 것
    // BoardProjection findByNo(long no);

    // 전체조회 + 글번호기준 내림차순 정렬
    List<Board> findAllByOrderByNoDesc();

    // 제목이 정확하게 일치하는 + 글번호기준 내림차순 정렬
    List<Board> findByTitleOrderByNoDesc(String title);

    // 제목에 단어가 포함하는 + 글번호기준 내림차순 정렬 + 페이지네이션
    List<Board> findByTitleContainingOrderByNoDesc(String title, Pageable pageable);

    // 제목에 단어 포함(대소문자구분하지 않음)
    // 글번호기준 내림차순
    // 페이지 네이션
    List<Board> findByTitleIgnoreCaseContainingOrderByNoDesc(String title, Pageable pageable);
    // 글번호기준 오름차순
    List<Board> findByTitleIgnoreCaseContainingOrderByNoAsc(String title, Pageable pageable);
    // 작성자에 단어포함(대소문자구분하지 않음)
    // 글번호 기준 내림차순
    List<Board> findByWriterIgnoreCaseContainingOrderByNoDesc(String title, Pageable pageable);
    // 글번호기준 오름차순
    List<Board> findByWriterIgnoreCaseContainingOrderByNoAsc(String title, Pageable pageable);

    // 제목에 단어가 포함된 전체 개수
    long countByTitleContaining(String title);
    long countByWriterContaining(String writer);

    // 이전글 현재글이 20번이면 작은것중에서 가장큰것 1개
    Optional<Board> findTop1ByNoLessThanOrderByNoDesc(long no);

    // 다음글 현재글이 20번이면 큰것중에서 가장 작은것 1개
    Optional<Board> findTop1ByNoGreaterThanOrderByNoAsc(Long no);
}
