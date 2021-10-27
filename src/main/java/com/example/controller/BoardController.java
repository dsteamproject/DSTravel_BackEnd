package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.entity.Board;
import com.example.repository.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/board")
public class BoardController {

    @Autowired
    private BoardRepository bRepository;

    // 게시판 목록
    @GetMapping(value = "/select_all", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectAll(
            @RequestParam(name = "title", required = false, defaultValue = "") String title,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<>();
        try {
            // List<Board> list = bRepository.findAllByOrderByNoDesc();

            // 페이지숫자(0부터), 개수
            PageRequest pageRequest = PageRequest.of(page - 1, size);
            List<Board> list = bRepository.findByTitleContainingOrderByNoDesc(title, pageRequest);
            long cnt = bRepository.countByTitleContaining(title);
            map.put("cnt", (cnt - 1) / size + 1);
            map.put("list", list);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 게시판 등록
    @PostMapping(value = "/insert", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> insertPost(@RequestBody Board board) {
        Map<String, Object> map = new HashMap<>();
        try {
            bRepository.save(board);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 상세페이지
    @GetMapping(value = "/selectone", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectOne(@RequestParam(name = "no", defaultValue = "0") long no) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (no == 0) {
                map.put("status", 300);
                map.put("error", "파라미터가 전달되지 않았습니다.");
            } else {
                Board board = bRepository.findById(no).orElseThrow();
                map.put("board", board);
                Optional<Board> prev = bRepository.findTop1ByNoLessThanOrderByNoDesc(no);
                if (prev.isPresent()) {
                    map.put("prev", prev.get().getNo());
                } else {
                    map.put("prev", 0);
                }
                Optional<Board> next = bRepository.findTop1ByNoGreaterThanOrderByNoAsc(no);
                if (next.isPresent()) {
                    map.put("next", next.get().getNo());
                } else {
                    map.put("next", 0);
                }
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // GET : 게시판 수정 페이지
    @GetMapping(value = "/update", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateGET(@RequestParam("no") long no) {
        Map<String, Object> map = new HashMap<>();
        try {
            Board board = bRepository.findById(no).orElseThrow();
            map.put("board", board);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // POST : 게시판 수정 (제목, 내용 + 필요사항 있을시 추가할 것)
    @PostMapping(value = "/update", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updatePost(@RequestBody Board board) {
        Map<String, Object> map = new HashMap<>();
        try {
            Board board1 = bRepository.findById(board.getNo()).orElseThrow();
            board1.setTitle(board.getTitle());
            board1.setContent(board.getContent());

            bRepository.save(board1);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 조회수 1 증가 (쿠키사용)
    @PutMapping(value = "/updateHit", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateHit(@RequestParam("no") long no, HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        try {
            Cookie oldCookie = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("postView")) {
                        oldCookie = cookie;
                    }
                }
            }
            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + no + "]")) {
                    Board board = bRepository.getById(no);
                    board.setHit(board.getHit() + 1);
                    bRepository.save(board);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + no + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24);
                    response.addCookie(oldCookie);
                }
            } else {
                Board board = bRepository.getById(no);
                board.setHit(board.getHit() + 1);
                bRepository.save(board);
                Cookie newCookie = new Cookie("postView", "[" + no + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(newCookie);
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

}
