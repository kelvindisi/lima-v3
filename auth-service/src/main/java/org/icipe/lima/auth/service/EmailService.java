package org.icipe.lima.auth.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
  @Value("${spring.mail.username}")
  private String fromEmail;
  private final JavaMailSender javaMailSender;

  /**
   * sends HTML email without any file attachment
   *
   * @param toEmail receiver of the email
   * @param subject of the email send to receiver
   * @param message main content of the email, preferably HTML table with inline styles
   */
  @Async
  public void sendHtmlEmail(String toEmail, String subject, String message) {
    try {
      MimeMessage mimeMessage = getMimeMessage();
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      messageHelper.setFrom(fromEmail);
      messageHelper.setTo(toEmail);
      messageHelper.setSubject(subject);
      messageHelper.setText(message, true);
      messageHelper.setReplyTo(fromEmail);
      javaMailSender.send(mimeMessage);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private MimeMessage getMimeMessage() {
    return javaMailSender.createMimeMessage();
  }
}
