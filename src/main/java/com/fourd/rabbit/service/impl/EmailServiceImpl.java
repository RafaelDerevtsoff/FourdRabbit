package com.fourd.rabbit.service.impl;

import com.fourd.rabbit.repository.TeacherRepository;
import com.fourd.rabbit.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private TeacherRepository teacherRepository;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public Mono<String> sendEmail(String recipientEmail, String subject, String body) {

        return Mono.fromRunnable(() -> {
                    senEmail(recipientEmail, subject, body);
                }).doOnSuccess(email -> {
                    log.info("Email Successfully send");
                })
                .thenReturn("User created");
    }

    private void senEmail(String recipientEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(System.getenv("EMAIL"));
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
