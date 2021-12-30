package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.example.dto.BoardDTO;
import com.example.dto.MemberDTO;
import com.example.entity.Board;
import com.example.entity.City;
import com.example.entity.Member;
import com.example.entity.Notice;
import com.example.entity.TD;
import com.example.entity.TodayVisitCount;
import com.example.entity.VisitorCount;
import com.example.entity.Worldcup;
import com.example.jwt.JwtUtil;
import com.example.mappers.BoardMapper;
import com.example.mappers.MemberMapper;
import com.example.repository.BoardRepository;
import com.example.repository.CityRepository;
import com.example.repository.MemberRepository;
import com.example.repository.TDRepository;
import com.example.repository.TodayVisitCountRepository;
import com.example.repository.VisitorCountRepository;
import com.example.repository.WorldcupRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
// 관리자 페이지
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	MemberRepository mRepository;

	@Autowired
	BoardRepository bRepository;

	@Autowired
	VisitorCountRepository vcRepository;

	@Autowired
	TDRepository tdRepository;

	@Autowired
	TodayVisitCountRepository tdvcRepository;

	@Autowired
	BoardMapper bMapper;

	@Autowired
	MemberMapper mMapper;

	@Autowired
	WorldcupRepository worldcupRepository;

	@Autowired
	CityRepository cityRepository;

	// 회원정보 조회
	// GET > http://localhost:8080/REST/admin/member
	@GetMapping(value = "/member", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> memberGET(@RequestHeader("token") String token,						// JWT token
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,			// 페이지번호
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,			// 1페이지당 게시물수
			@RequestParam(name = "state", required = false) String state,							// 0 : 삭제된 게시물 , 1 : 등록된 게시물
			@RequestParam(name = "orderbytype", defaultValue = "regdate") String orderbytype,		// 정렬시킬 column명
			@RequestParam(name = "orderby", defaultValue = "DESC") String orderby,					// DESC : 오름차순, ASC : 내림차순
			@RequestParam(name = "keywordtype", defaultValue = "ID") String keywordtype,			// 검색타입
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {	// 검색키워드
		Map<String, Object> map = new HashMap<>();
		try {
			// 토큰 값으로 member정보 추출
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();

			// 로그인된 사용자 검증
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				
				// ListMember 메소드 호출
				List<MemberDTO> memberlist = mMapper.ListMember(keywordtype, keyword, 1 + (size * (page - 1)),
						page * size, orderbytype, orderby, state);
				// ListMemberTotalCount 메소드 호출
				int cnt = mMapper.ListMemberTotalCount(keywordtype, keyword, state);
				map.put("cnt", (cnt - 1) / size + 1);
				map.put("memberlist", memberlist);
				map.put("status", 200);
				map.put("data", "회원정보 조회를 성공했습니다.");

			} else {
				map.put("status", 578);
				map.put("data", "회원정보 조회를 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "회원정보 조회를 실패했습니다.");
		}
		return map;

	}

	// 회원정보 수정
	// PUT > http://localhost:8080/REST/admin/memberupdate
	@PutMapping(value = "/memberupdate", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> memberPUT(@RequestHeader("token") String token 			// JWT token
			, @RequestBody Member[] member1) {											// 수정할 member 정보
		Map<String, Object> map = new HashMap<>();
		try {
			// 토큰 값으로 member정보 추출
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			// 로그인된 사용자 검증
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				
				for (Member mem : member1) {
					// Member 객체 설정
					Member member2 = mRepository.findById(mem.getId()).get();
					member2.setEmail(mem.getEmail());
					member2.setGender(mem.getGender());
					member2.setName(mem.getName());
					member2.setNicname(mem.getNicname());
					member2.setRole(mem.getRole());
					member2.setState(mem.getState());
					// Member 객체 변경사항 저장
					mRepository.save(member2);
				}
				map.put("status", 200);
				map.put("data", "회원정보 수정을 성공했습니다.");

			} else {
				map.put("status", 578);
				map.put("data", "회원정보 수정을 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "회원정보 수정을 실패했습니다.");
		}
		return map;
	}

	// 회원정보 삭제
	// PUT > http://localhost:8080/REST/admin/memberdelete
	@PutMapping(value = "/memberdelete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> memberDELETE(@RequestHeader("token") String token, @RequestBody Member[] member1) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 토큰 값으로 member정보 추출
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			// 로그인된 사용자 검증
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				
				for (Member mem : member1) {
					// Member 객체 설정
					Member member2 = mRepository.findById(mem.getId()).get();
					member2.setEmail("");
					member2.setPassword("");
					member2.setNewpw("");
					member2.setGender("");
					member2.setName("");
					member2.setNicname("");
					member2.setLogin("");
					member2.setRole("");
					member2.setToken("");
					member2.setState(0);
					// Member 객체 저장
					mRepository.save(member2);
				}
				map.put("status", 200);
				map.put("data", "회원정보 삭제를 완료하였습니다.");

			} else {
				map.put("status", 578);
				map.put("data", "회원정보 삭제를 실패하였습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "회원정보 삭제를 실패하였습니다.");
		}
		return map;
	}

	// 게시물 조회
	// GET > http://localhost:8080/REST/admin/board
	@GetMapping(value = "/board", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardGET(@RequestHeader("token") String token,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "state", required = false) String state,
			@RequestParam(name = "orderbytype", defaultValue = "NO") String orderbytype,
			@RequestParam(name = "orderby", defaultValue = "DESC") String orderby,
			@RequestParam(name = "keywordtype", defaultValue = "TITLE") String keywordtype,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 토큰 값으로 member정보 추출 
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			// 로그인된 사용자 검증
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				// ListBoard 메소드 호출
				List<BoardDTO> boardlist = bMapper.ListBoard(keywordtype, keyword, category,
						1 + (size * (page - 1)), page * size, orderbytype, orderby, state);
				// ListBoardTotalCount 메소드 호출
				int cnt = bMapper.ListBoardTotalCount(keywordtype, keyword, category, state);
				map.put("cnt", (cnt - 1) / size + 1);
				map.put("boardlist", boardlist);
				map.put("status", 200);
				map.put("data", "게시글 조회를 성공하였습니다.");
			} else {
				map.put("status", 578);
				map.put("data", "게시글 조회를 실패하였습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "게시글 조회를 실패하였습니다.");
		}
		return map;
	}

	// 게시물 내용 조회
	// GET > http://localhost:8080/REST/admin/board/selectOne
	@GetMapping(value = "/board/selectOne", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardselectOne(@RequestHeader("token") String token, @RequestParam("no") Long no) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 토큰 값으로 member정보 추출 
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			// 로그인된 사용자 검증
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				// BoardSelectOneAdmin 메소드 호출
				BoardDTO board = bMapper.BoardSelectOneAdmin(no);
				map.put("board", board);
				map.put("status", 200);
				map.put("data", "게시물 조회에 성공하였습니다.");
			} else {
				map.put("status", 578);
				map.put("data", "게시물 조회에 실패하였습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "게시물 조회에 실패하였습니다.");
		}
		return map;
	}

	// 게시물 내용 수정
	// PUT > http://localhost:8080/REST/admin/boardupdate
	@PutMapping(value = "/boardupdate", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardPUT(@RequestHeader("token") String token, @RequestBody Board board) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 토큰 값으로 member정보 추출 
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			// 로그인된 사용자 검증
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				// 글번호에 해당하는 게시글 조회 
				Board board1 = bRepository.findById(board.getNo()).get();
				board1.setCategory(board.getCategory());
				board1.setContent(board.getContent());
				board1.setTitle(board.getTitle());
				// 변경된 객체 저장
				bRepository.save(board1);
				map.put("status", 200);
				map.put("data", "게시물 수정에 성공하였습니다.");
			} else {
				map.put("status", 578);
				map.put("data", "게시물 수정에 실패하였습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "게시물 수정에 실패하였습니다.");
		}
		return map;
	}

	// 게시물 삭제
	// PUT > http://localhost:8080/REST/admin/board/delete
	@PutMapping(value = "/board/delete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardDelete(@RequestHeader("token") String token, @RequestParam(name = "no") String no) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 토큰 값으로 member정보 추출 
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			// 로그인된 사용자 검증
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				// 게시글 번호로 Admindelete 메소드 호출
				int result = bMapper.Admindelete(no);
				if (result != 1) {
					map.put("status", 901);
					map.put("data", "게시글 삭제에 실패했습니다.");
				} else {
					map.put("status", 200);
					map.put("data", "게시글 삭제에 성공했습니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("data", "게시글 삭제에 실패했습니다.");
		}
		return map;
	}

	// 게시글 복원
	// PUT > http://localhost:8080/REST/admin/board/update
	@PutMapping(value = "/board/update", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardupdate(@RequestHeader("token") String token, @RequestParam(name = "no") long no) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Board board = bRepository.querySelectByIdstate0(no);
				board.setState(1);
				bRepository.save(board);
				map.put("board", board);
				map.put("status", 200);

			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", e.hashCode());
		}
		return map;
	}

	// 방문자수 저장
	@PostMapping(value = "/visitorCount", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> visitorPOST(@RequestHeader("token") String token,
			@RequestBody VisitorCount visitorCount) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				vcRepository.save(visitorCount);
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

	// 총 방문자 데이터
	@GetMapping(value = "/visitorTotal", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> visitorGET(@RequestHeader("token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				List<VisitorCount> visitorCount = vcRepository.findAll();
				map.put("totalVisitor", visitorCount);
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

	// 접속자 카운터
	@PostMapping(value = "/todayVisitor", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> todayVisitorPOST(@RequestHeader("token") String token,
			@RequestBody TodayVisitCount visitorCount) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				tdvcRepository.save(visitorCount);
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

	@GetMapping(value = "/todayTotal", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> todayTotalGet(@RequestHeader("token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				List<TodayVisitCount> todayVisitCount = tdvcRepository.findAll();
				map.put("todaycount", todayVisitCount);
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

	// 여행지 임시저장 List
	@GetMapping(value = "/TDtem", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> TDtemGET(@RequestHeader("token") String token,
			@RequestParam(name = "state", required = false) int state) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				List<TD> list = tdRepository.selectAdminTDtem(state);
				map.put("AdminTDtemList", list);
				map.put("status", 200);

			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", e.hashCode());
		}
		return map;
	}

	// 여행지 임시저장 요청처리 (state=0 => 반려 state=2, 승인 state=1)
	@PutMapping(value = "/TDtem", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> TDtemPUT(@RequestHeader("token") String token, @RequestParam(name = "no") Integer no,
			@RequestParam(name = "review") String review) {
		Map<String, Object> map = new HashMap<>();
		try {

			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				if (review.equals("Approval")) {
					tdRepository.queryTDtemApproval(no);
					map.put("result", "승인 완료");
					map.put("status", 200);
				} else if (review.equals("Companion")) {
					tdRepository.queryTDtemCompanion(no);
					map.put("result", "반려 처리");
					map.put("status", 200);
				} else {
					map.put("result", "review 값이 잘못되었습니다. (Approval or Companion 으로 전송필요)");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", e.hashCode());
		}
		return map;
	}

	@PostMapping(value = "/warning", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> warningPost(@RequestHeader("token") String token, @RequestBody Board board) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				Notice notice = new Notice();
				notice.setMember(board.getMember());
				notice.setNotice("신고 누적으로 관리자가" + board.getNo() + "번 :" + board.getTitle() + "의 게시물을 삭제할 수 있습니다.");
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

	// 월드컵 지역별 1등수
	@GetMapping(value = "/worldcup", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> worldcup(@RequestHeader("token") String token, @RequestParam("city") Integer city) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				City city1 = cityRepository.getById(city);
				List<Worldcup> list = worldcupRepository.findByTd_city(city1);
				Map<String, Object> map1 = new HashMap<>();
				for(Worldcup worldcup : list){
					map1.put(worldcup.getTd().getTitle(), worldcupRepository.countByTd_no(worldcup.getTd().getNo()));
				}
				List<Map<String,Object>> list2 = new ArrayList<>();
				for(Entry<String, Object> elem : map1.entrySet()){ 
					Map<String, Object> map2 = new HashMap<>();
					map2.put("title", elem.getKey());
					map2.put("cnt", elem.getValue());
					list2.add(map2);
				}
				map.put("list", list2);
				map.put("total", list.size());
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