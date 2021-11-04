package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.entity.Member;
import com.example.entity.MemberImg;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberImgRepository;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private ResourceLoader resourceLoader;

    @Value("${default.image}")
    private String DEFAULTIMAGE;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    MemberImgRepository mImgRepository;

    // 127.0.0.1:8080/REST/mypage/select_image?no=
    // 이미지주소
    @GetMapping(value = "/select_image")
    public ResponseEntity<byte[]> selectImage(@RequestParam("no") long no) throws IOException {
        try {
            MemberImg mImg = mImgRepository.getById(no);
            if (mImg.getImage().length > 0) {
                HttpHeaders headers = new HttpHeaders();
                if (mImg.getImagetype().equals("image/jpeg")) {
                    headers.setContentType(MediaType.IMAGE_JPEG);
                } else if (mImg.getImagetype().equals("image/png")) {
                    headers.setContentType(MediaType.IMAGE_PNG);
                } else if (mImg.getImagetype().equals("image/gif")) {
                    headers.setContentType(MediaType.IMAGE_GIF);
                }

                // 클래스명 response = new 클래스명( 생성자선택 )
                ResponseEntity<byte[]> response = new ResponseEntity<>(mImg.getImage(), headers, HttpStatus.OK);
                return response;
            }
            return null;
        }
        // 오라클에 이미지를 읽을 수 없을 경우
        catch (Exception e) {
            InputStream is = resourceLoader.getResource(DEFAULTIMAGE).getInputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            ResponseEntity<byte[]> response = new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
            return response;
        }
    }

    // 이미지 등록(수정)
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
