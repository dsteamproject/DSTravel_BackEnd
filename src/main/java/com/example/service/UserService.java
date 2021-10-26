package com.example.service;

import com.example.entity.Member;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    MemberRepository mRepository;

    public boolean userEmailCheck(String userEmail, String userName) {

        Member member = mRepository.findByEmail(userEmail);
        if (member != null && member.getName().equals(userName)) {
            return true;
        } else {
            return false;
        }
    }
}
