package org.icipe.lima.auth.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.icipe.lima.auth.entity.AbstractEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "user_validation_token")
public class UserVerificationToken extends AbstractEntity {
  @Column(name = "verification_token")
  private String token;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private AppUser user;
}
