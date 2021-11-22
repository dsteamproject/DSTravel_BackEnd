package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dto.BoardDTO;
import com.example.entity.Board;
import com.example.entity.Member;
import com.example.entity.TD;
import com.example.entity.TodayVisitCount;
import com.example.entity.VisitorCount;
import com.example.jwt.JwtUtil;
import com.example.mappers.BoardMapper;
import com.example.repository.BoardRepository;
import com.example.repository.MemberRepository;
import com.example.repository.TDRepository;
import com.example.repository.TodayVisitCountRepository;
import com.example.repository.VisitorCountRepository;

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

	// 모든 회원정보
	@GetMapping(value = "/member", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> memberGET(@RequestHeader("token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				List<Member> memberlist = mRepository.findAll();
				map.put("memberlist", memberlist);
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

	// 아직 구현중 ( 어떤 데이터를 수정할지 정해야 함. )
	// 회원정보 수정
	@PutMapping(value = "/memberupdate", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> memberPUT(@RequestHeader("token") String token, @RequestBody Member member1) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				// Member member2 = mRepository.findById(member1.getId());
				// member2.set
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

	// 모든 게시물데이터
	// 페이지네이션 타이틀검색 카테고리별 분류
	// 필수 Parmeter : 헤더(토큰)
	// Parmeter : 페이지번호, 페이지당사이즈, 카테고리명, 게시물상태(0 or 1), 검색키워드
	// 성공 return : 200, boardlist, cnt(게시물수/페이지당사이즈)
	@GetMapping(value = "/board", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardGET(@RequestHeader("token") String token,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "state", required = false) String state,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(category);
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {

				List<BoardDTO> boardlist = bMapper.selectBoardAdmin(keyword, category, 1 + (size * (page - 1)),
						page * size, state);

				int cnt = bMapper.CountBoardAdmin(keyword, category, state);
				map.put("cnt", (cnt - 1) / size + 1);
				map.put("boardlist", boardlist);
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

	// 게시물 내용 삭제(STATE = 0 일때만 삭제됨)
	// Parmeter : 삭제할 게시물 번호
	// return : 성공 200, 실패 901
	@PutMapping(value = "/board/delete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardDelete(@RequestHeader("token") String token, @RequestParam(name = "no") String no) {
		Map<String, Object> map = new HashMap<>();
		try {

			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {

				int result = bMapper.Admindelete(no);
				if (result != 1) {
					map.put("status", 901);
				} else {
					map.put("status", 200);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", e.hashCode());
		}
		return map;
	}

	// 게시물 state=0 => state=1 로 복원
	// Parmeter : param(no)
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

	// 방문자수 저장(방문자수만 넘어오면 됨. 하루에 한번만 넘어와야함.)
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
	public Map<String, Object> TDtemGET(@RequestHeader("token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {
				List<TD> list = tdRepository.selectAdminTDtem();
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
}