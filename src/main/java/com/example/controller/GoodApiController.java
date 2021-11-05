package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.Board;
import com.example.entity.Good;
import com.example.entity.Member;
import com.example.entity.MemberProjection;
import com.example.jwt.JwtUtil;
import com.example.repository.BoardRepository;
import com.example.repository.GoodRepository;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/good")
public class GoodApiController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    GoodRepository goodRepository;

    @Autowired
    BoardRepository bRepository;

    // 좋아요 추가하고 좋아요 갯수 전달
    @PostMapping(value = "/board", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> addgood(@RequestHeader("TOKEN") String token, @RequestBody Board board) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.getById(id);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                Good good = new Good();
                if (goodRepository.queryselectgood(board, member) == null) {
                    good.setBoard(board);
                    good.setMember(member);
                    goodRepository.save(good);

                } else {
                    Good good1 = goodRepository.queryselectgood(board, member);
                    good1.setBoard(null);
                    good1.setMember(null);
                    goodRepository.delete(good1);
                }
                int goodCnt = goodRepository.queryCountByBoard(board);
                map.put("status", 200);
                map.put("good", goodCnt);
            } else {
                map.put("status", 578);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

}
