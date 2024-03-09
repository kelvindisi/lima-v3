package org.icipe.lima.auth.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.icipe.lima.auth.entity.user.AppUser;
import org.icipe.lima.auth.entity.user.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class SecurityUser implements UserDetails {
  private AppUser user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

    for (Authority authority : user.getAuthorities()) {
      grantedAuthorities.add(
          new SimpleGrantedAuthority("ROLE_" + authority.getAuthority().toUpperCase()));
      authority
          .getRoles()
          .forEach(
              role -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
              });
    }

    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return user.isActive();
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return user.isVerified();
  }

  @Override
  public boolean isEnabled() {
    return user.isVerified();
  }
}
