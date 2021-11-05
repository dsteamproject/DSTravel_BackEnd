package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.entity.Board;
import com.example.entity.Member;
import com.example.entity.Reply;
import com.example.jwt.JwtUtil;
import com.example.repository.BoardRepository;
import com.example.repository.GoodRepository;
import com.example.repository.MemberRepository;
import com.example.repository.ReplyRepository;

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
    ReplyRepository reRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    GoodRepository goodRepository;

    // 게시판 목록
    @GetMapping(value = "/select_all", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectAll(
            @RequestParam(name = "category", required = false, defaultValue = "review") String category,
            @RequestParam(name = "type", required = false, defaultValue = "title") String type,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "orderby", required = false, defaultValue = "latest") String orderby) {
        Map<String, Object> map = new HashMap<>();
        try {

            PageRequest pageRequest = PageRequest.of(page - 1, size);
            // System.out.println(type);
            // List<Board> list = bRepository.querySelectAllByWriterOrderByAsc(type,
            // keyword, category, pageRequest);
            // map.put("list", list);
            if (orderby.equals("latest") && type.equals("title")) {
                List<Board> list = bRepository.querySelectAllByTitleOrderByDesc(keyword, category, pageRequest);

                for (Board board : list) {
                    System.out.println(board.getNo() + "," + goodRepository.countByBoard_no(board.getNo()));
                }

                map.put("list", list);
                int cnt = bRepository.queryCountByTitle(keyword, category);
                map.put("cnt", (cnt - 1) / size + 1);
            } else if (orderby.equals("latest") && type.equals("writer")) {
                List<Board> list = bRepository.querySelectAllByWriterOrderByDesc(keyword, category, pageRequest);
                map.put("list", list);
                int cnt = bRepository.queryCountByWriter(keyword, category);
                map.put("cnt", (cnt - 1) / size + 1);
            } else if (orderby.equals("old") && type.equals("title")) {
                List<Board> list = bRepository.querySelectAllByTitleOrderByAsc(keyword, category, pageRequest);
                map.put("list", list);
                int cnt = bRepository.queryCountByTitle(keyword, category);
                map.put("cnt", (cnt - 1) / size + 1);
            } else if (orderby.equals("old") && type.equals("writer")) {
                List<Board> list = bRepository.querySelectAllByWriterOrderByAsc(keyword, category, pageRequest);
                map.put("list", list);
                int cnt = bRepository.queryCountByWriter(keyword, category);
                map.put("cnt", (cnt - 1) / size + 1);
            }
            map.put("status", 200);
        } catch (

        Exception e) {
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
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.getById(id);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                board.setWriter(id);
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

    // 해당 번호로 게시물 조회 후 category가 일치하면 조회 불일치시 800 오류 (접속경로잘못됨) <<필요한 작업인지 다시한번 확인필요
    // 상세페이지
    @GetMapping(value = "/selectone", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectOne(@RequestHeader(required = false, name = "TOKEN") String token,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "no", defaultValue = "0") long no) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (no == 0) {
                map.put("status", 300);
            } else {
                Board board = bRepository.querySelectById(no);
                List<Reply> replylist = reRepository.querySelectReplyAllByBno(no);
                if (token != null) {
                    map.put("LoginId", jwtUtil.extractUsername(token.substring(6)));
                }
                goodRepository.queryCountByBoard(board);
                map.put("good", goodRepository);
                map.put("reply", replylist);
                map.put("board", board);
                Board prev = bRepository.queryByCategoryTop1OrderByNoDesc(category, no);
                if (prev != null) {
                    map.put("prev", prev.getNo());
                } else {
                    map.put("prev", 0);
                }
                Board next = bRepository.queryByCategoryTop1OrderByNoAsc(category, no);
                if (next != null) {
                    map.put("next", next.getNo());
                } else {
                    map.put("next", 0);
                }
                map.put("status", 200);
            }
        } catch (

        Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // GET : 게시판 수정 페이지
    @GetMapping(value = "/update", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateGET(@RequestHeader("TOKEN") String token, @RequestParam("no") long no) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            if (mRepository.findById(id).isPresent() && !jwtUtil.isTokenExpired(token.substring(6))) {
                Board board = bRepository.querySelectById(no);
                map.put("board", board);
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

    // 게시판 수정 (제목, 내용 + 필요사항 있을시 추가할 것)
    @PutMapping(value = "/update", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updatePost(@RequestBody Board board, @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.getById(id);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                Board board1 = bRepository.querySelectById(board.getNo());
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

    // 게시판 삭제 (state =0)
    @PutMapping(value = "/delete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteOne(@RequestHeader("TOKEN") String token,
            @RequestParam(name = "no", defaultValue = "0") long no) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.getById(id);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                if (no == 0) {
                    map.put("status", 300);
                } else {
                    bRepository.queryDelete(no);
                    map.put("status", 200);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 조회수 1 증가 (쿠키사용) (state=1인 게시물만)
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
                    Board board = bRepository.querySelectById(no);
                    board.setHit(board.getHit() + 1);
                    bRepository.save(board);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + no + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24);
                    response.addCookie(oldCookie);
                }
            } else {
                Board board = bRepository.querySelectById(no);
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

    // 댓글등록
    @PostMapping(value = "/reply", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> replyPost(@RequestHeader(required = false, name = "TOKEN") String token,
            @RequestParam("no") Long no, @RequestBody Reply reply) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (token != null) {
                String id = jwtUtil.extractUsername(token.substring(6));
                Member member = mRepository.getById(id);
                if (member != null && member.getToken().equals(token.substring(6))
                        && !jwtUtil.isTokenExpired(token.substring(6))) {
                    Reply newReply = new Reply();
                    newReply.setBoard(bRepository.querySelectById(no));
                    newReply.setReply(reply.getReply());
                    newReply.setWriter(id);
                    reRepository.save(newReply);

                    int countreply = reRepository.queryCountSelectReply(no);
                    Board board = bRepository.querySelectById(no);
                    board.setCountreply(countreply);
                    bRepository.save(board);
                    map.put("status", 200);
                } else {
                    map.put("status", 578);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", map.hashCode());
        }
        return map;

    }

    // 댓글 삭제
    @PutMapping(value = "/reply_delete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> replyDeleteOne(@RequestHeader("TOKEN") String token, @RequestParam("no") Long no,
            @RequestBody Board board) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.getById(id);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                if (no == 0) {
                    map.put("status", 300);
                } else {
                    reRepository.queryReplyDelete(no);

                    int countreply = reRepository.queryCountSelectReply(no);
                    Board board1 = bRepository.querySelectById(board.getNo());
                    board1.setCountreply(countreply);
                    bRepository.save(board1);
                    map.put("status", 200);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 댓글 수정
    @PutMapping(value = "/reply_update", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> replyUpdate(@RequestHeader("TOKEN") String token, @RequestBody Reply reply) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.getById(id);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                Reply reply1 = reRepository.querySelectReply(reply.getNo());
                reply1.setReply(reply.getReply());
                reRepository.save(reply1);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

}
