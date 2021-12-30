package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.entity.Board;
import com.example.entity.BoardImg;
import com.example.entity.Good;
import com.example.entity.Member;
import com.example.entity.Reply;
import com.example.entity.TDSave;
import com.example.entity.Warning;
import com.example.jwt.JwtUtil;
import com.example.repository.BoardImgRepository;
import com.example.repository.BoardRepository;
import com.example.repository.GoodRepository;
import com.example.repository.MemberRepository;
import com.example.repository.ReplyRepository;
import com.example.repository.TDSaveRepository;
import com.example.repository.WarningRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/board")
public class BoardController {

	@Value("${default.image}")
	private String DEFAULTIMAGE;

	@Autowired
	private BoardRepository bRepository;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	MemberRepository mRepository;

	@Autowired
	ReplyRepository reRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	GoodRepository goodRepository;

	@Autowired
	BoardImgRepository bImgRepository;

	@Autowired
	WarningRepository warningRepository;

	@Autowired
	TDSaveRepository tdSaveRepository;

	// 게시판 목록
	// GET > http://localhost:8080/REST/board/select_all
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
			// PageRequest 객체 생성
			PageRequest pageRequest = PageRequest.of(page - 1, size);
			// 검색타입:글제목, 정렬:내림차순
			if (orderby.equals("latest") && type.equals("title")) {
				// 게시물(여행일정) 조회 (state=2 : 여행일정 공유허용, state=1 : 여행일정 미공유, state=0 : 여행일정 삭제)
				if(category.equals("TDsave")){
					List<TDSave> list = tdSaveRepository.querySelectAllByTitleOrderByDesc(keyword, pageRequest);
					List<Map<String, Object>> list1 = new ArrayList<>();
					ObjectMapper mapper = new ObjectMapper();
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map1 = mapper.readValue(list.get(i).getTd(),
								new TypeReference<Map<String, Object>>() {
								});
						map1.put("No", list.get(i).getNo());
						map1.put("title", list.get(i).getTitle());
						map1.put("member", list.get(i).getMember());
						map1.put("state", list.get(i).getState());
						list1.add(map1);
					}
					map.put("list", list1);
					int cnt = tdSaveRepository.queryCountByTitle(keyword);
					map.put("cnt", (cnt -1) / size + 1);
				}
				// 게시물 조회
				else{
				List<Board> list = bRepository.querySelectAllByTitleOrderByDesc(keyword, category, pageRequest);
				map.put("list", list);
				int cnt = bRepository.queryCountByTitle(keyword, category);
				map.put("cnt", (cnt - 1) / size + 1);
				}
			// 검색타입:작성자, 정렬:내림차순
			} else if (orderby.equals("latest") && type.equals("writer")) {
				// 게시물(여행일정) 조회
				if(category.equals("TDsave")){
					List<TDSave> list = tdSaveRepository.querySelectAllByWriterOrderByDesc(keyword,  pageRequest);
					List<Map<String, Object>> list1 = new ArrayList<>();
					ObjectMapper mapper = new ObjectMapper();
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map1 = mapper.readValue(list.get(i).getTd(),
								new TypeReference<Map<String, Object>>() {
								});
						map1.put("No", list.get(i).getNo());
						map1.put("title", list.get(i).getTitle());
						map1.put("member", list.get(i).getMember());
						map1.put("state", list.get(i).getState());
						list1.add(map1);
					}
					map.put("list", list1);
					int cnt = tdSaveRepository.queryCountByWriter(keyword);
					map.put("cnt", (cnt -1) / size + 1);
				}
				// 게시물 조회
				else{
					List<Board> list = bRepository.querySelectAllByWriterOrderByDesc(keyword, category, pageRequest);
					map.put("list", list);
					int cnt = bRepository.queryCountByWriter(keyword, category);
					map.put("cnt", (cnt - 1) / size + 1);
				}
			// 검색타입:글제목, 정렬:오름차순
			} else if (orderby.equals("old") && type.equals("title")) {
				// 게시물(여행일정) 조회
				if(category.equals("TDsave")){
					List<TDSave> list = tdSaveRepository.querySelectAllByTitleOrderByAsc(keyword,  pageRequest);
					List<Map<String, Object>> list1 = new ArrayList<>();
					ObjectMapper mapper = new ObjectMapper();
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map1 = mapper.readValue(list.get(i).getTd(),
								new TypeReference<Map<String, Object>>() {
								});
						map1.put("No", list.get(i).getNo());
						map1.put("title", list.get(i).getTitle());
						map1.put("member", list.get(i).getMember());
						map1.put("state", list.get(i).getState());
						list1.add(map1);
					}
					map.put("list", list1);
					int cnt = tdSaveRepository.queryCountByTitle(keyword);
					map.put("cnt", (cnt -1) / size + 1);
				}
				// 게시물 조회
				else{
					List<Board> list = bRepository.querySelectAllByTitleOrderByAsc(keyword, category, pageRequest);
					map.put("list", list);
					int cnt = bRepository.queryCountByTitle(keyword, category);
					map.put("cnt", (cnt - 1) / size + 1);
				}
			// 검색타입:작성자, 정렬:오름차순
			} else if (orderby.equals("old") && type.equals("writer")) {
				// 게시물(여행일정) 조회
				if(category.equals("TDsave")){
					List<TDSave> list = tdSaveRepository.querySelectAllByWriterOrderByAsc(keyword,pageRequest);
					List<Map<String, Object>> list1 = new ArrayList<>();
					ObjectMapper mapper = new ObjectMapper();
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map1 = mapper.readValue(list.get(i).getTd(),
								new TypeReference<Map<String, Object>>() {
								});
						map1.put("No", list.get(i).getNo());
						map1.put("title", list.get(i).getTitle());
						map1.put("member", list.get(i).getMember());
						map1.put("state", list.get(i).getState());
						list1.add(map1);
					}
					map.put("list", list1);
					int cnt = tdSaveRepository.queryCountByWriter(keyword);
					map.put("cnt", (cnt -1) / size + 1);
				}
				// 게시물 조회
				else{
					List<Board> list = bRepository.querySelectAllByWriterOrderByAsc(keyword, category, pageRequest);
					map.put("list", list);
					int cnt = bRepository.queryCountByWriter(keyword, category);
					map.put("cnt", (cnt - 1) / size + 1);
				}
			}
			map.put("status", 200);
			map.put("data", "게시물 조회에 성공하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "게시물 조회에 실패하였습니다.");
		}
		// 결과 값 리턴
		return map;
	}

	// 게시판 등록
	@PostMapping(value = "/insert", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insertPost(@ModelAttribute Board board, @RequestHeader("TOKEN") String token,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				System.out.println(board.getCategory());
				Board board1 = new Board();
				board1.setCategory(board.getCategory());
				board1.setContent(board.getContent());
				board1.setTitle(board.getTitle());
				board1.setMember(member);
				if (file != null) {
					BoardImg boardImg = new BoardImg();
					boardImg.setBoard(bRepository.save(board1));
					boardImg.setImage(file.getBytes());
					boardImg.setImagename(file.getOriginalFilename());
					boardImg.setImagetype(file.getContentType());
					boardImg.setImagesize(file.getSize());
					bImgRepository.save(boardImg);
				} else {
					bRepository.save(board1);
				}

				map.put("status", 200);
			} else {
				map.put("status", 578);
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			map.put("status", e.hashCode());
		}
		return map;
	}

	// 게시글 이미지 조회
	// GET > http://localhost:8080/REST/board/select_image?no=
	@GetMapping(value = "/select_image")
	public ResponseEntity<byte[]> selectImage(@RequestParam(name = "no") Board board) throws IOException {
		try {
			// 게시글 이미지 조회
			BoardImg bImg = bImgRepository.querySelectBoardImg(board);
			
			// 이미지 타입 확인
			if (bImg.getImage().length > 0) {
				HttpHeaders headers = new HttpHeaders();
				if (bImg.getImagetype().equals("image/jpeg")) {
					headers.setContentType(MediaType.IMAGE_JPEG);
				} else if (bImg.getImagetype().equals("image/png")) {
					headers.setContentType(MediaType.IMAGE_PNG);
				} else if (bImg.getImagetype().equals("image/gif")) {
					headers.setContentType(MediaType.IMAGE_GIF);
				}

				// response 객체 생성
				ResponseEntity<byte[]> response = new ResponseEntity<>(bImg.getImage(), headers, HttpStatus.OK);

				// 이미지 리턴
				return response;
			}
			// 타입이 맞지않을 경우 null값 리턴
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
				if(category.equals("TDsave")){
					TDSave tdsave = tdSaveRepository.querySelectByIdstate12(no);
					map.put("tdsave", tdsave);
				}
				else{
					Board board = bRepository.querySelectByIdstate1(no);
					map.put("board", board);
					List<Reply> replylist = reRepository.querySelectReplyAllByBno(no);
					map.put("reply", replylist);
					int CntGood = goodRepository.queryCountByBoard(board);
					map.put("CntGood", CntGood);
				}
				if (token != null) {
					map.put("LoginId", jwtUtil.extractUsername(token.substring(6)));
				}
				if(category.equals("TDsave")){
					TDSave prev = tdSaveRepository.queryByCategoryTop1OrderByNoDesc(no);
					if (prev != null) {
						map.put("prev", prev.getNo());
					} else {
						map.put("prev", 0);
					}
				}else{
					Board prev = bRepository.queryByCategoryTop1OrderByNoDesc(category, no);
					if (prev != null) {
						map.put("prev", prev.getNo());
					} else {
						map.put("prev", 0);
					}
				}
				if(category.equals("TDsave")){
					TDSave next = tdSaveRepository.queryByCategoryTop1OrderByNoAsc(no);
					if (next != null) {
						map.put("next", next.getNo());
					} else {
						map.put("next", 0);
					}
				}else{
					Board next = bRepository.queryByCategoryTop1OrderByNoAsc(category, no);
					if (next != null) {
						map.put("next", next.getNo());
					} else {
						map.put("next", 0);
					}
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
	public Map<String, Object> updateGET(@RequestHeader("TOKEN") String token, @RequestParam("no") long no) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Board board = bRepository.querySelectByIdstate1(no);
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
	public Map<String, Object> updatePost(@RequestParam(name = "file", required = false) MultipartFile file,
			@ModelAttribute Board board, @RequestHeader(name = "TOKEN") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Board board1 = bRepository.querySelectByIdstate1(board.getNo());
				board1.setTitle(board.getTitle());
				board1.setContent(board.getContent());
				bRepository.save(board1);
				if (file != null) {
					BoardImg bImg = bImgRepository.querySelectBoardImg(board);
					bImg.setImage(file.getBytes());
					bImg.setImagename(file.getOriginalFilename());
					bImg.setImagesize(file.getSize());
					bImg.setImagetype(file.getContentType());
					bImgRepository.save(bImg);
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

	// 게시판 삭제 (state =0)
	@PutMapping(value = "/delete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteOne(@RequestHeader("TOKEN") String token,
			@RequestParam(name = "no", defaultValue = "0") long no) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
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
					Board board = bRepository.querySelectByIdstate1(no);
					board.setHit(board.getHit() + 1);
					bRepository.save(board);
					oldCookie.setValue(oldCookie.getValue() + "_[" + no + "]");
					oldCookie.setPath("/");
					oldCookie.setMaxAge(60 * 60 * 24);
					response.addCookie(oldCookie);
				}
			} else {
				Board board = bRepository.querySelectByIdstate1(no);
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
				Member member = mRepository.findById(id).orElseThrow();
				// map.put("a", reply);
				if (member != null && member.getToken().equals(token.substring(6))
						&& !jwtUtil.isTokenExpired(token.substring(6))) {
					Reply newReply = new Reply();
					newReply.setBoard(bRepository.querySelectByIdstate1(no));
					newReply.setReplycontent(reply.getReplycontent());
					newReply.setMember(member);
					reRepository.save(newReply);

					Board board = bRepository.querySelectByIdstate1(no);
					board.setReply(reRepository.queryCountSelectReply(no));
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
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				if (no == 0) {
					map.put("status", 300);
				} else {
					reRepository.queryReplyDelete(no);
					int countreply = reRepository.queryCountSelectReply(board.getNo());
					System.out.println(countreply);
					Board board1 = bRepository.querySelectByIdstate1(board.getNo());
					board1.setReply(countreply);
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
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Reply reply1 = reRepository.querySelectReply(reply.getNo());
				reply1.setReplycontent(reply.getReplycontent());
				reRepository.save(reply1);
				map.put("status", 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", e.hashCode());
		}
		return map;
	}

	// 좋아요 기능
	// Header : token 필요
	// Body : 게시판 번호 필요
	// return : 같은아이디로 1번누르면 좋아요1증가 2번누르면 좋아요1감소
	@PostMapping(value = "/good", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> addgood(@RequestHeader("TOKEN") String token, @RequestBody Board board) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Good good = new Good();
				if (goodRepository.queryselectgood(board, member) == null) {
					good.setBoard(board);
					good.setMember(member);
					goodRepository.save(good);
					Board board1 = bRepository.querySelectByIdstate1(board.getNo());
					board1.setGood(goodRepository.countByBoard_no(board.getNo()));
					bRepository.save(board1);
					map.put("goodresult", goodRepository.queryselectgoodstate(board, member).isPresent());

				} else {
					Good good1 = goodRepository.queryselectgood(board, member);
					good1.setBoard(null);
					good1.setMember(null);
					goodRepository.delete(good1);
					Board board1 = bRepository.querySelectByIdstate1(board.getNo());
					board1.setGood(goodRepository.countByBoard_no(board.getNo()));
					bRepository.save(board1);
					map.put("goodresult", goodRepository.queryselectgoodstate(board, member).isPresent());
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

	// 좋아요 버튼 적용확인(꽉찬하트 true, 빈하트 false)
	@PostMapping(value = "/good/state", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> addgoodstate(@RequestHeader("TOKEN") String token, @RequestBody Board board) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Board board1 = bRepository.querySelectByIdstate1(board.getNo());
				map.put("goodresult", goodRepository.queryselectgoodstate(board1, member).isPresent());
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

	// 신고 기능
	// Header : token 필요
	// Body : 게시판 번호 필요
	// return : 같은아이디로 1번누르면 신고1증가, 2번누르면 신고1감소
	@PostMapping(value = "/warning", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> addwarning(@RequestHeader("TOKEN") String token, @RequestBody Board board) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Warning warning = new Warning();
				if (warningRepository.queryselectwarning(board, member) == null) {
					warning.setBoard(board);
					warning.setMember(member);
					warningRepository.save(warning);
					Board board1 = bRepository.querySelectByIdstate1(board.getNo());
					board1.setWarning(warningRepository.countByBoard_no(board.getNo()));
					bRepository.save(board1);
					map.put("warningresult", warningRepository.queryselectwarningstate(board, member).isPresent());

				} else {
					Warning warning1 = warningRepository.queryselectwarning(board, member);
					warning1.setBoard(null);
					warning1.setMember(null);
					warningRepository.delete(warning1);
					Board board1 = bRepository.querySelectByIdstate1(board.getNo());
					board1.setWarning(warningRepository.countByBoard_no(board.getNo()));
					bRepository.save(board1);
					map.put("warningresult", warningRepository.queryselectwarningstate(board, member).isPresent());
				}
				int WarningCnt = warningRepository.queryCountWarningByBoard(board);
				map.put("status", 200);
				map.put("warning", WarningCnt);
			} else {
				map.put("status", 578);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", e.hashCode());
		}
		return map;
	}

	// 신고 버튼 적용확인(꽉찬신고 true, 빈신고 false)
	@PostMapping(value = "/warning/state", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> addwarningstate(@RequestHeader("TOKEN") String token, @RequestBody Board board) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Board board1 = bRepository.querySelectByIdstate1(board.getNo());
				map.put("warningresult", warningRepository.queryselectwarningstate(board1, member).isPresent());
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
