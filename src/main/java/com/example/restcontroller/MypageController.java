package com.example.restcontroller;

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
import com.example.entity.TDSave;
import com.example.entity.TDType;
import com.example.jwt.JwtUtil;
import com.example.mappers.GoodMapper;
import com.example.repository.BoardRepository;
import com.example.repository.GoodRepository;
import com.example.repository.MemberImgRepository;
import com.example.repository.MemberRepository;
import com.example.repository.TDRepository;
import com.example.repository.TDSaveRepository;
import com.example.repository.TypeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    TDSaveRepository tdsaveRepository;

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
    // ???????????????
    @GetMapping(value = "/select_image")
    public ResponseEntity<byte[]> selectImage(@RequestParam("id") String member) throws IOException {
        try {
            Member member1 = mRepository.findById(member).get();
            MemberImg mImg = mImgRepository.querySelectByMemberId(member1);
            if (mImg.getImage().length > 0) {
                HttpHeaders headers = new HttpHeaders();
                if (mImg.getImagetype().equals("image/jpeg")) {
                    headers.setContentType(MediaType.IMAGE_JPEG);
                } else if (mImg.getImagetype().equals("image/png")) {
                    headers.setContentType(MediaType.IMAGE_PNG);
                } else if (mImg.getImagetype().equals("image/gif")) {
                    headers.setContentType(MediaType.IMAGE_GIF);
                }

                // ???????????? response = new ????????????( ??????????????? )
                ResponseEntity<byte[]> response = new ResponseEntity<>(mImg.getImage(), headers, HttpStatus.OK);
                return response;
            }
            return null;
        }
        // ???????????? ???????????? ?????? ??? ?????? ??????
        catch (Exception e) {
            InputStream is = resourceLoader.getResource(DEFAULTIMAGE).getInputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            ResponseEntity<byte[]> response = new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
            return response;
        }
    }

    // ????????? ??????(??????)
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
                    map.put("result", "????????????");
                } else {
                    memberimg.setMember(member);
                    mImgRepository.queryinsert(memberimg);
                    map.put("result", "????????????");
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

    // ??????????????? ???
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

    // ???????????? ?????? ( body : {password: ????????????, newpw: ???????????????} )
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

    // ???????????? ??????
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

    // ?????? ????????? ?????????
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

    // ?????? ???????????? ?????????
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
                    if (list.size() >= (page * size)) {
                        for (int i = ((page - 1) * size); i < (page * size); i++) {
                            Board board = bRepository.findById(list.get(i).getBoard()).get();
                            list1.add(board);
                        }
                    } else {
                        for (int i = ((page - 1) * size); i < list.size(); i++) {
                            Board board = bRepository.findById(list.get(i).getBoard()).get();
                            list1.add(board);
                        }
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

    // ?????? ???????????? ?????????,??????,?????????
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
                TDType type1 = typeRepository.findById(type).get();
                List<GoodDTO> list = goodMapper.selectGoodTD(member1.getId());
                // System.out.println(list);
                List<TD> list1 = new ArrayList<>();
                for(int i=0; i<list.size();i++){
                    TD td = tdRepository.selectGoodType(list.get(i).getTd(), type1);
                    if(td!=null){
                        list1.add(td);
                    }
                }
                List<TD> list2 = new ArrayList<>();    
                if (list1.size() >= size) {
                    if (list1.size() >= (page * size)) {
                        for (int i = ((page - 1) * size); i < (page * size); i++) {
                                list2.add(list1.get(i));
                        }
                    } else {
                        for (int i = ((page - 1) * size); i < list.size(); i++) {
                                list2.add(list1.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < list1.size(); i++) {
                        list2.add(list1.get(i));
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

    // ?????? ????????? ?????????
    @GetMapping(value = "mytdtem")
    public Map<String, Object> myTDtemGET(@RequestHeader("TOKEN") String token,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member1 = mRepository.findById(id).orElseThrow();
            if (member1 != null && member1.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                PageRequest pageRequest = PageRequest.of(page - 1, size);
                List<TD> list = tdRepository.selectMyTDtem(id, pageRequest);
                int cnt = tdRepository.selectCountMyTDtem(id);
                map.put("total", cnt);
                map.put("cnt", (cnt - 1) / size + 1);
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

    // ????????????
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

    // ?????? ????????? ?????????
    @GetMapping(value = "/tdsave", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> tdsave(@RequestHeader("TOKEN") String token,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<>();
        try {
            String id = jwtUtil.extractUsername(token.substring(6));
            Member member = mRepository.findById(id).orElseThrow();
            if (member != null && member.getToken().equals(token.substring(6))
                    && !jwtUtil.isTokenExpired(token.substring(6))) {
                List<TDSave> list = tdsaveRepository.selectMyTDsave(member);
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> list1 = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map1 = mapper.readValue(list.get(i).getTd(),
                            new TypeReference<Map<String, Object>>() {
                            });
                    map1.put("title", list.get(i).getTitle());
                    map1.put("member", list.get(i).getMember());
                    map1.put("state", list.get(i).getState());
                    list1.add(map1);
                }
                List<Map<String, Object>> list2 = new ArrayList<>();
                if (list1.size() >= size) {
                    if (list1.size() >= (page * size)) {
                        for (int i = ((page - 1) * size); i < (page * size); i++) {
                            list2.add(list1.get(i));
                        }
                    } else {
                        for (int i = ((page - 1) * size); i < list.size(); i++) {
                            list2.add(list1.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        list2.add(list1.get(i));
                    }
                }
                map.put("total", list1.size());
                map.put("cnt", (list1.size() - 1) / size + 1);
                map.put("tdsave", list2);
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
