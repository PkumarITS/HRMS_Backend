package com.phegondev.usersmanagementsystem.service;


import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;

@Service
public class MailService {
	
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendStatusUpdate(String to, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Timesheet Status Update");
        message.setText("Your timesheet status has been updated to: " + status);
        mailSender.send(message);
    }
    
    public void sendEmail(String to, List<String> cc, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); 

        helper.setTo(to);
        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.toArray(new String[0]));
        }
        helper.setSubject(subject);
        helper.setText(body, false); // false = plain text

        mailSender.send(message);
    }
    
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false); // false = plain text

        mailSender.send(message);
        System.out.println("Email sent to " + to + " with subject: " + subject);
    }
	
	

}
