package com.dune.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    public String sendTextEmail(String recipient, String subject, String content) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(recipient);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(content);
            javaMailSender.send(simpleMailMessage);
            return "Email has been sent to " + recipient + " successfully.";
        }
        catch(Exception e) {
            return "Error to send email: " + e.getLocalizedMessage();
        }
    }
}