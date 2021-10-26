package com.example.service;

import com.example.entity.MailDto;
import com.example.entity.Member;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SendEmailService {

    @Autowired
    MemberRepository mRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    private static final String FROM_ADDRESS = "ghtjd026@gmail.com";

    public void updatePassword(String str, String userEmail) {
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        String pw = bcpe.encode(str);
        Member member = mRepository.findByEmail(userEmail);
        member.setPassword(pw);
        mRepository.save(member);
    }

    public String getTempPassword() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void sendEmailTempPassword(String userEmail, String userName) {
        String str = getTempPassword();
        updatePassword(str, userEmail);
        MailDto dto = new MailDto();
        dto.setAddress(userEmail);
        dto.setTitle(userName + "님의 Travel 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Travel 임시비밀번호 안내 관련 이메일 입니다." + "[" + userName + "]" + "님의 임시 비밀번호는 " + str + " 입니다.");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getAddress());
        message.setFrom(SendEmailService.FROM_ADDRESS);
        message.setSubject(dto.getTitle());
        message.setText(dto.getMessage());
        javaMailSender.send(message);
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo(userEmail);
        // message.setFrom("ghtjd026@gmail.com");
        // message.setSubject(userName + "님의 Travel 임시비밀번호 안내 이메일 입니다.");
        // message.setText("안녕하세요. Travel 임시비밀번호 안내 관련 이메일 입니다." + "[" + userName + "]"
        // + "님의 임시 비밀번호는 " + str + " 입니다.");
        // javaMailSender.send(message);
        System.out.println("이메일 전송 완료!");
    }

    // $2a$10$k5bOfebGJ7r2GGmnYSV8yeEJm8RJvsgnYx6YnDp/RPPgnl3/T4LO6
    // $2a$10$k5bOfebGJ7r2GGmnYSV8yeEJm8RJvsgnYx6YnDp/RPPgnl3/T4LO6
    // $2a$10$EV3QmeKbyUmRgGt859s6ouZm8u0uOebzPz4lsw41YVgy9n72GWYW.
}
