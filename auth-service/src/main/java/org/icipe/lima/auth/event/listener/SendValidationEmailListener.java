package org.icipe.lima.auth.event.listener;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.groovy.util.Maps;
import org.icipe.lima.auth.entity.user.AppUser;
import org.icipe.lima.auth.entity.user.UserVerificationToken;
import org.icipe.lima.auth.event.SendValidationEmailEvent;
import org.icipe.lima.auth.service.EmailService;
import org.icipe.lima.auth.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class SendValidationEmailListener implements ApplicationListener<SendValidationEmailEvent> {
  @Value("${auth.token.expiresAtDays}")
  private int tokenExpiresAt;

  private final UserService userService;
  private final TemplateEngine templateEngine;
  private final EmailService emailService;

  @Async
  @Override
  public void onApplicationEvent(SendValidationEmailEvent event) {
    LocalDateTime expiresAt = LocalDateTime.now().plusDays(tokenExpiresAt);
    AppUser user = event.getUser();

    UserVerificationToken verificationToken =
        UserVerificationToken.builder()
            .token(UUID.randomUUID().toString())
            .expiresAt(expiresAt)
            .user(user)
            .build();

    String firstName = user.getFirstName();
    String userEmail = user.getEmail();

    userService.saveUserVerificationToken(verificationToken);

    //    TODO -> change this to application public url since application might be running on a container
    String verificationLink =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath("/auth/verification/" + verificationToken.getToken())
            .build()
            .toUriString();

    sendVerificationMail(firstName, userEmail, verificationLink);
  }

  /*
  This method will generate an HTML email for verification using Thymeleaf template to insert required parameters
  which includes the name of receiver and token on the template.
  Call emailService with two arguments, receiver email and the HTML verification email
   */
  private void sendVerificationMail(String name, String userEmail, String verificationLink) {
    Context context = new Context();
    context.setVariables(Maps.of("name", name, "verificationLink", verificationLink));
    String verificationEmail = templateEngine.process("email/user_email_verification", context);
    emailService.sendHtmlEmail(userEmail, "User Account Verification", verificationEmail);
  }
}
