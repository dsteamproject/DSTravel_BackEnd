package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.Board;
import com.example.entity.Member;
import com.example.entity.TodayVisitCount;
import com.example.entity.VisitorCount;
import com.example.jwt.JwtUtil;
import com.example.repository.BoardRepository;
import com.example.repository.MemberRepository;
import com.example.repository.TodayVisitCountRepository;
import com.example.repository.VisitorCountRepository;

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
	TodayVisitCountRepository tdvcRepository;

	// hsyu
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
	@GetMapping(value = "/board", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> boardGET(@RequestHeader("token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
			String id = jwtUtil.extractUsername(token.substring(6));
			Member member = mRepository.findById(id).orElseThrow();
			if (member != null && member.getToken().equals(token.substring(6))
					&& !jwtUtil.isTokenExpired(token.substring(6))) {

				List<Board> boardlist = bRepository.findAll();
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
}