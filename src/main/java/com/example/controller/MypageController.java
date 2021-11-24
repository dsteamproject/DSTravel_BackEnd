package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dto.GoodDTO;
import com.example.entity.Board;
import com.example.entity.Member;
import com.example.entity.MemberImg;
import com.example.entity.TD;
import com.example.entity.Type;
import com.example.jwt.JwtUtil;
import com.example.mappers.GoodMapper;
import com.example.repository.BoardRepository;
import com.example.repository.GoodRepository;
import com.example.repository.MemberImgRepository;
import com.example.repository.MemberRepository;
import com.example.repository.TDRepository;
import com.example.repository.TypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/mypage")
public class MypageController {

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${default.image}")
    private String DEFAULTIMAGE;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    TDRepository tdRepository;

    @Autowired
    BoardRepository bRepository;

    @Autowired
    MemberImgRepository mImgRepository;

    @Autowired
    GoodRepository goodRepository;

    @Autowired
    GoodMapper goodMapper;

    // 127.0.0.1:8080/REST/mypage/select_image?id=
    // 이미지주소
    @GetMapping(value = "/select_image")
    public ResponseEntity<byte[]> selectImage(@RequestParam("id") Member member) throws IOException {
        try {
            MemberImg mImg = mImgRepository.querySelectByMemberId(member);
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
    @PutMapping(value = "/insertMemberImg", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberimgPut(@RequestHeader("TOKEN") String token,
            @RequestParam(name = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            // System.out.println(token);
            String id = jwtUtil.extractUsername(token.substring(6));
            // System.out.println("--------------------------------" + id);
            Member member = mRepository.findById(id).orElseThrow();
            // System.out.println(member);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                MemberImg memberimg = new MemberImg();
                memberimg.setImage(file.getBytes());
                memberimg.setImagename(file.getOriginalFilename());
                memberimg.setImagesize(file.getSize());
                memberimg.setImagetype(file.getContentType());
                if (mImgRepository.querySelectByMemberId(member) != null) {
                    mImgRepository.queryupdate(memberimg, member);
                    map.put("result", "수정완료");
                } else {
                    memberimg.setMember(member);
                    mImgRepository.queryinsert(memberimg);
                    map.put("result", "등록완료");
                }
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

    // 마이페이지 홈
    @GetMapping(value = "/home")
    public Map<String, Object> home(@RequestHeader("TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member1 = mRepository.findById(id).orElseThrow();
            if (member1 != null && member1.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                map.put("member", mRepository.querySelectByid(id).get());
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

    // 비밀번호 변경 ( body : {password: 기존암호, newpw: 변경할암호} )
    @PutMapping(value = "/memberpwchange", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberpwchange(@RequestHeader("TOKEN") String token, @RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member1 = mRepository.findById(id).orElseThrow();
            BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
            if (member1 != null && member1.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                if (bcpe.matches(member.getPassword(), member1.getPassword())) {
                    member1.setPassword(bcpe.encode(member.getNewpw()));
                    mRepository.save(member1);
                    map.put("status", 200);
                } else {
                    map.put("status", 301);
                }

            } else {
                map.put("status", 578);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 회원정보 변경
    @PutMapping(value = "/memberchange", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberchange(@RequestHeader("TOKEN") String token, @RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member1 = mRepository.findById(id).orElseThrow();
            if (member1 != null && member1.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                member1.setName(member.getName());
                member1.setNicname(member.getNicname());
                member1.setEmail(member.getEmail());
                member1.setGender(member.getGender());
                mRepository.save(member1);
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

    // 내가 등록한 게시물
    @GetMapping(value = "/myboard")
    public Map<String, Object> myboardGET(@RequestHeader("TOKEN") String token,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.findById(id).orElseThrow();
            System.out.println(member);
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                PageRequest pageRequest = PageRequest.of(page - 1, size);
                List<Board> list = bRepository.findAllByMember(member, pageRequest);
                int cnt = bRepository.countByMember(member);
                map.put("total", cnt);
                map.put("cnt", (cnt - 1) / size + 1);
                map.put("list", list);
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

    // 내가 좋아요한 게시물
    @GetMapping(value = "mygoodboard")
    public Map<String, Object> mygoodboardGET(@RequestHeader("TOKEN") String token,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member1 = mRepository.findById(id).orElseThrow();
            if (member1 != null && member1.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {

                List<GoodDTO> list = goodMapper.selectGoodBoard(member1.getId());
                List<Board> list1 = new ArrayList<>();
                if (list.size() >= size) {
                    for (int i = ((page - 1) * size); i < (page * size); i++) {
                        Board board = bRepository.findById(list.get(i).getBoard()).get();
                        list1.add(board);
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        Board board = bRepository.findById(list.get(i).getBoard()).get();
                        list1.add(board);
                    }
                }
                map.put("total", list.size());
                map.put("cnt", (list.size() - 1) / size + 1);
                map.put("board", list1);
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

    // 내가 좋아요한 여행지,숙소,음식점
    @GetMapping(value = "mygoodtd")
    public Map<String, Object> mygoodTDGET(@RequestHeader("TOKEN") String token, @RequestParam("type") Integer type,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member1 = mRepository.findById(id).orElseThrow();
            if (member1 != null && member1.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                Type type1 = typeRepository.findById(type).get();
                List<GoodDTO> list = goodMapper.selectGoodTD(member1.getId());
                System.out.println(list);
                List<TD> list1 = new ArrayList<>();
                if (list.size() >= size) {
                    for (int i = ((page - 1) * size); i < (page * size); i++) {
                        TD td = tdRepository.selectGoodType(list.get(i).getTd(), type1);
                        if (td != null) {
                            list1.add(td);
                        }
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        TD td = tdRepository.selectGoodType(list.get(i).getTd(), type1);
                        if (td != null) {
                            list1.add(td);
                        }
                    }
                }
                map.put("total", list1.size());
                map.put("cnt", (list1.size() - 1) / size + 1);
                map.put("td", list1);
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

    // 내가 요청한 여행지
    @GetMapping(value = "mytdtem")
    public Map<String, Object> myTDtemGET(@RequestHeader("TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member1 = mRepository.findById(id).orElseThrow();
            if (member1 != null && member1.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {

                List<TD> list = tdRepository.selectMyTDtem(id);
                map.put("mytdtem", list);
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

    // 회원탈퇴
    @PutMapping(value = "/memberdelete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> memberdelete(@RequestHeader("TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.findById(id).orElseThrow();
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                member.setState(0);
                mRepository.save(member);
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
