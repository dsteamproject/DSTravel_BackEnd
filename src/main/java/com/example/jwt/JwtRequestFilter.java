// package com.example.jwt;

// import java.io.IOException;

// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// import com.example.service.MemberDetailsService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// // nodejs checkToken
// // 인증서가 유효하면 컨트롤러로 진입함
// @Component
// public class JwtRequestFilter extends OncePerRequestFilter {

//     @Autowired
//     JwtUtil jwtUtil;

//     @Autowired
//     MemberDetailsService mService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         try {
//             // const headers = {"Content-Type":"application/json", "token" :'123456_aaaa'}
//             String token = request.getHeader("token");
//             String username = null;

//             if (token != null) {
//                 username = jwtUtil.extractUsername(token.substring(6));
//             }

//             // 토큰은 전달되었고 로그인이 되어야 되는 것만!!
//             if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//                 // 시큐리티에 로그인 처리루틴
//                 UserDetails userDetails = mService.loadUserByUsername(username);

//                 if (jwtUtil.validateToken(token, userDetails.getUsername())) {
//                     UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails,
//                             null, userDetails.getAuthorities());
//                     upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                     SecurityContextHolder.getContext().setAuthentication(upat);
//                 }
//             }

//             // 토큰 유무와 관련없이 컨트롤러로 넘겨
//             filterChain.doFilter(request, response);
//         } catch (Exception e) {
//             // status : 578, message : '토큰오류'
//             response.sendError(578, "토큰오류");
//         }
//     }

// }