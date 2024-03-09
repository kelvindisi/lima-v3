package org.icipe.lima.auth.entity.client;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.icipe.lima.auth.entity.AbstractEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(
    name = "clients",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UQ_CLIENT_ID",
          columnNames = {"client_id"})
    })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Client extends AbstractEntity {
  @Column(name = "client_id")
  private String clientId;

  @Column(name = "client_name")
  private String clientName;

  @Column(name = "client_secret")
  private String clientSecret;

  @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<RedirectUri> redirectUris;

  @ManyToMany(mappedBy = "clients", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<AuthenticationMethod> authenticationMethods;

  @ManyToMany(mappedBy = "clients", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<GrantType> grantTypes;

  @ManyToMany(mappedBy = "clients", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<Scope> scopes;

  public static RegisteredClient fromClient(Client client) {
    return RegisteredClient.withId(client.getId())
        .clientId(client.clientId)
        .clientName(client.clientName)
        .clientSecret(client.clientSecret)
        .clientAuthenticationMethods(AuthenticationMethod.from(client.getAuthenticationMethods()))
        .authorizationGrantTypes(GrantType.from(client.getGrantTypes()))
        .redirectUris(
            c ->
                c.addAll(
                    client.redirectUris.stream()
                        .map(RedirectUri::getUri)
                        .collect(Collectors.toSet())))
        .scopes(Scope.from(client.getScopes()))
        .build();
  }
}
