package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.Member;
import com.example.entity.MemberImg;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberImgRepository;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/mypage")
public class MypageController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    MemberImgRepository mImgRepository;

    @PutMapping(value = "/insertMemberImg")
    public Map<String, Object> memberimgPut(@RequestHeader("TOKEN") String token, @ModelAttribute MemberImg memberImg,
            @RequestParam(name = "file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.getById(id);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                memberImg.setImage(file.getBytes());
                memberImg.setImagename(file.getOriginalFilename());
                memberImg.setImagesize(file.getSize());
                memberImg.setImagetype(file.getContentType());
                memberImg.setMember(member);
                mImgRepository.save(memberImg);
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

}
