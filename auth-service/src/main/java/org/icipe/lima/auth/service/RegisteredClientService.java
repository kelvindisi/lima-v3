package org.icipe.lima.auth.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.icipe.lima.auth.entity.client.AuthenticationMethod;
import org.icipe.lima.auth.entity.client.Client;
import org.icipe.lima.auth.entity.client.GrantType;
import org.icipe.lima.auth.entity.client.RedirectUri;
import org.icipe.lima.auth.entity.client.Scope;
import org.icipe.lima.auth.repository.client.AuthenticationMethodRepository;
import org.icipe.lima.auth.repository.client.ClientRepository;
import org.icipe.lima.auth.repository.client.GrantTypeRepository;
import org.icipe.lima.auth.repository.client.ScopeRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegisteredClientService implements RegisteredClientRepository {
  private final PasswordEncoder passwordEncoder;

  private final AuthenticationMethodRepository authenticationMethodRepository;
  private final ClientRepository clientRepository;
  private final GrantTypeRepository grantTypeRepository;
  private final ScopeRepository scopeRepository;

  @Transactional
  @Override
  public void save(RegisteredClient registeredClient) {
    Client c =
        Client.builder()
            .clientId(registeredClient.getClientId())
            .clientName(registeredClient.getClientName())
            .clientSecret(passwordEncoder.encode(registeredClient.getClientSecret()))
            .build();
    c.setRedirectUris(RedirectUri.from(c, registeredClient.getRedirectUris()));
    c.setAuthenticationMethods(
        getAuthenticationMethod(c, registeredClient.getClientAuthenticationMethods()));
    c.setGrantTypes(getGrantTypes(c, registeredClient.getAuthorizationGrantTypes()));
    c.setScopes(getScopes(c, registeredClient.getScopes()));

    clientRepository.save(c);
  }

  @Override
  public RegisteredClient findById(String id) {
    Client client =
        clientRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("wrong client details or does not exist"));

    return Client.fromClient(client);
  }

  @Override
  public RegisteredClient findByClientId(String clientId) {
    Client client =
        clientRepository
            .findClientByClientId(clientId)
            .orElseThrow(() -> new RuntimeException("wrong client details or does not exist"));
    return Client.fromClient(client);
  }

  private List<Scope> getScopes(Client client, Set<String> scopes) {
    return scopes.stream()
        .map(
            s -> {
              var optionalScope = scopeRepository.findScopeByScopeName(s);
              if (optionalScope.isPresent()) {
                optionalScope.get().getClients().add(client);
                return optionalScope.get();
              }
              return Scope.builder().scopeName(s).clients(List.of(client)).build();
            })
        .toList();
  }

  private List<GrantType> getGrantTypes(Client client, Set<AuthorizationGrantType> grantTypes) {
    return grantTypes.stream()
        .map(
            grantType -> {
              var optionalGrantType =
                  grantTypeRepository.findGrantTypeByGrantType(grantType.getValue());
              if (optionalGrantType.isPresent()) {
                optionalGrantType.get().getClients().add(client);
                return optionalGrantType.get();
              }
              return GrantType.builder()
                  .grantType(grantType.getValue())
                  .clients(List.of(client))
                  .build();
            })
        .toList();
  }

  /**
   * gets authentication method if exists or builds a new one if it doesn't and assigns client
   *
   * @param client to be assigned to authentication to help in populating intermediary table
   * @param authenticationMethods list of ClientAuthenticationMethod objs assigned to client
   * @return list of AppAuthenticationMethod that are initialized with client details
   */
  private List<AuthenticationMethod> getAuthenticationMethod(
      Client client, Set<ClientAuthenticationMethod> authenticationMethods) {

    return authenticationMethods.stream()
        .map(
            authenticationMethod -> {
              var optionalMethod =
                  authenticationMethodRepository.findAuthenticationMethodByMethod(
                      authenticationMethod.getValue());
              if (optionalMethod.isPresent()) {
                optionalMethod.get().getClients().add(client);
                return optionalMethod.get();
              }
              return AuthenticationMethod.builder()
                  .method(authenticationMethod.getValue())
                  .clients(List.of(client))
                  .build();
            })
        .toList();
  }

  public List<Client> findAll() {
    return clientRepository.findAll();
  }
}
