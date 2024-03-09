package org.icipe.lima.auth.service;

import lombok.RequiredArgsConstructor;
import org.icipe.lima.auth.entity.user.AppUser;
import org.icipe.lima.auth.entity.user.UserVerificationToken;
import org.icipe.lima.auth.event.SendValidationEmailEvent;
import org.icipe.lima.auth.repository.user.UserRepository;
import org.icipe.lima.auth.repository.user.UserVerificationTokenRepository;
import org.icipe.lima.auth.request.CreateUserAccount;
import org.icipe.lima.auth.security.SecurityUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ApplicationEventPublisher eventPublisher;
  private final UserVerificationTokenRepository verificationTokenRepository;

  public void createNewAccount(CreateUserAccount createAccount) {
    AppUser user = new AppUser();
    user.setFirstName(createAccount.firstName());
    user.setLastName(createAccount.lastName());
    user.setEmail(createAccount.email());
    user.setPassword(passwordEncoder.encode(createAccount.password()));

    userRepository.save(user);
    //    send verification email
    eventPublisher.publishEvent(new SendValidationEmailEvent(this, user));
  }

  public void saveUserVerificationToken(UserVerificationToken verificationToken) {
    verificationTokenRepository.save(verificationToken);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var appUser =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("wrong login credentials"));
    return new SecurityUser(appUser);
  }
}
