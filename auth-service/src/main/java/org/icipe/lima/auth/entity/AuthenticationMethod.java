package org.icipe.lima.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "authentication_method")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AuthenticationMethod extends AbstractEntity {
  @Column(name = "method")
  private String method;

  @ManyToMany
  @JoinTable(
      name = "client_authentication_method",
      joinColumns = @JoinColumn(name = "authentication_method_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
  private List<Client> clients = new ArrayList<>();

  public static Consumer<Set<ClientAuthenticationMethod>> from(
      List<AuthenticationMethod> authenticationMethods) {
    return c ->
        c.addAll(
            authenticationMethods.stream()
                .map(
                    authenticationMethod ->
                        new ClientAuthenticationMethod(authenticationMethod.method))
                .collect(Collectors.toSet()));
  }
}
