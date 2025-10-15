package com.gege.ideas.messenger.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

   private final JavaMailSender mailSender;

   public MailService(JavaMailSender mailSender) {
      this.mailSender = mailSender;
   }

   public void sendSimpleEmail(String to, String subject, String text)
      throws MessagingException, UnsupportedEncodingException {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

      helper.setFrom(new InternetAddress("no-reply@zen-vy.com", "Zenvy"));
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text, true); // true = HTML content

      mailSender.send(message);
   }
}
