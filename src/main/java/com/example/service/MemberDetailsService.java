package com.example.service;

import java.util.Collection;

import com.example.entity.Member;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티에서 로그인 시 UserDetailsService 인터페이스의
// 메소드 loadUserByUsername이 자동으로 호출됨
// 여기에 필요한 기능을 구현하고 User를 리턴시킴
@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    MemberRepository mRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = mRepository.findById(username).get();

        // String의 권한을 Collection<>으로 변환
        String[] role = { member.getRole() };
        Collection<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(role);

        // 이메일, 암호, 권한 정보를 리턴
        // import ...security.core.userdetails.User;
        User user = new User(member.getEmail(), member.getPassword(), roles);
        return user;
    }

}
