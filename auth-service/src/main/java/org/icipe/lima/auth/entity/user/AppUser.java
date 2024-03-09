package org.icipe.lima.auth.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.icipe.lima.auth.entity.AbstractEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AppUser extends AbstractEntity {
  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "phoneNumber")
  private String phoneNumber;

  @Column(name = "password")
  private String password;

  @Column(name = "verified")
  private boolean verified = false;

  @Column(name = "active")
  private boolean active = false;

  @Column(name = "locked")
  private boolean locked = false;

  @JsonIgnore
  @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
  private List<Authority> authorities;
}
