package org.icipe.lima.auth.repository.user;

import org.icipe.lima.auth.entity.user.UserVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, String> {}
