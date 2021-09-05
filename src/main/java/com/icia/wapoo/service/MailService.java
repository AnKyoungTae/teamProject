package com.icia.wapoo.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    @Autowired
    private final MemberService memberService;

    private JavaMailSender mailSender;
    private final String FROM_ADDRESS = "jellycisko@gmail.com";


    public void simpleMailSend(String id) {

        SimpleMailMessage message = new SimpleMailMessage();

        // 업데이트하고 아이디와 새로운비밀번호를 전송한다.
        message.setTo(id + "@gmail.com");
        message.setFrom(FROM_ADDRESS);
        message.setSubject("제목없음이지롱");
        message.setText("내용을 뭘까요");

        mailSender.send(message);
    }
}
