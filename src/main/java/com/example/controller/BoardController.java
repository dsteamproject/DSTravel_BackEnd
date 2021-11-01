package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.entity.Board;
import com.example.jwt.JwtUtil;
import com.example.repository.BoardRepository;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/board")
public class BoardController {

    @Autowired
    private BoardRepository bRepository;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 게시판 목록
    @GetMapping(value = "/select_all", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectAll(
            @RequestParam(name = "type", required = false, defaultValue = "title") String type,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "orderby", required = false, defaultValue = "latest") String orderby) {
        Map<String, Object> map = new HashMap<>();
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, size);
            if (orderby.equals("latest") && type.equals("title")) {
                List<Board> list = bRepository.findByTitleIgnoreCaseContainingOrderByNoDesc(keyword, pageRequest);
                map.put("list", list);
                long cnt = bRepository.countByTitleContaining(keyword);
                map.put("cnt", (cnt - 1) / size + 1);
            } else if (orderby.equals("latest") && type.equals("writer")) {
                List<Board> list = bRepository.findByWriterIgnoreCaseContainingOrderByNoDesc(keyword, pageRequest);
                map.put("list", list);
                long cnt = bRepository.countByWriterContaining(keyword);
                map.put("cnt", (cnt - 1) / size + 1);
            } else if (orderby.equals("old") && type.equals("title")) {
                List<Board> list = bRepository.findByTitleIgnoreCaseContainingOrderByNoAsc(keyword, pageRequest);
                map.put("list", list);
                long cnt = bRepository.countByTitleContaining(keyword);
                map.put("cnt", (cnt - 1) / size + 1);
            } else if (orderby.equals("old") && type.equals("writer")) {
                List<Board> list = bRepository.findByWriterIgnoreCaseContainingOrderByNoAsc(keyword, pageRequest);
                map.put("list", list);
                long cnt = bRepository.countByWriterContaining(keyword);
                map.put("cnt", (cnt - 1) / size + 1);
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 게시판 등록
    @PostMapping(value = "/insert", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> insertPost(@RequestBody Board board, @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token.substring(6)))) {
                board.setWriter(jwtUtil.extractUsername(token.substring(6)));
                bRepository.save(board);
                map.put("status", 200);
            } else {
                map.put("status", 578);
            }
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
    public Map<String, Object> updatePost(@RequestBody Board board, @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token.substring(6)))) {
                Board board1 = bRepository.findById(board.getNo()).orElseThrow();
                board1.setTitle(board.getTitle());
                board1.setContent(board.getContent());
                bRepository.save(board1);
                map.put("status", 200);
            } else {
                map.put("status", 578);
            }
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
