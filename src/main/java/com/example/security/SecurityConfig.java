package com.example.security;

import com.example.service.MemberDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration // 환경설정 파일
@EnableWebSecurity // security를 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 새로운 기능을 추가한 DetailsService사용
    @Autowired
    MemberDetailsService mService;

    // 환경설정에서 객체만들기
    // 회원가입 암호방식을 로그인시 같은 방식으로 적용해야 하기 때문에
    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    // 인증방식은 MemberDetailsService를 사용하고, 암호화방식은
    // 위에 만든 @Bean객체 방식으로 사용
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mService).passwordEncoder(encode());
    }

    // @Autowired
    // private JwtRequestFilter jwtRequestFilter;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/admin", "/admin/*").hasAnyRole("ADMIN").anyRequest().permitAll();

        // // 필터 추가
        // http.addFilterBefore(jwtRequestFilter,
        // UsernamePasswordAuthenticationFilter.class);

        // // session 저장 방법
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // h2-console 사용
        // 크롬에서 127.0.0.1:8080/REST/h2-console
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
    }
}