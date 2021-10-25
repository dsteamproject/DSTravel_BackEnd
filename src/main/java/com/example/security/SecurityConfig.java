package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 환경설정 파일
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // REST-CONTROLLER
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // http.addFilterBefore(jwtRequestFilter,
        // UsernamePasswordAuthenticationFilter.class);

        // H2-CONSOLE, RESTFUL
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        // ALL
        http.authorizeRequests().anyRequest().permitAll().and();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }
}
