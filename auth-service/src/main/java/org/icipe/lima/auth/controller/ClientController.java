package org.icipe.lima.auth.controller;

import static org.icipe.lima.auth.helper.AuthorizationUtils.getAuthenticationMethods;
import static org.icipe.lima.auth.helper.AuthorizationUtils.getGrantTypes;
import static org.icipe.lima.auth.helper.AuthorizationUtils.getScopes;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.icipe.lima.auth.entity.Client;
import org.icipe.lima.auth.request.CreateClientRequest;
import org.icipe.lima.auth.service.RegisteredClientService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
  private final RegisteredClientService clientService;

  //  TODO -> Introduce pagination
  @GetMapping
  public String getAllClients(Model model) {
    List<Client> clients = clientService.findAll();
    model.addAttribute("clients", clients);

    return "dashboard/clients/all_clients";
  }

  @GetMapping("/create")
  public String createClient(
      @ModelAttribute("createClient") CreateClientRequest createClientRequest, Model model) {
    model.addAttribute("authenticationMethods", getAuthenticationMethods());
    model.addAttribute("grantTypes", getGrantTypes());
    model.addAttribute("scopes", getScopes());

    return "dashboard/clients/create_client";
  }

  @PostMapping("/create")
  public String saveClient(
      @Valid @ModelAttribute("createClient") CreateClientRequest createClientRequest,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("authenticationMethods", getAuthenticationMethods());
      model.addAttribute("grantTypes", getGrantTypes());
      model.addAttribute("scopes", getScopes());

      return "dashboard/clients/create_client";
    }
    //    call service to save client
    RegisteredClient client = buildClientFromRequest(createClientRequest);
    clientService.save(client);
    redirectAttributes.addFlashAttribute("message", "Client created successfully");
    return "redirect:/clients";
  }

  //  TODO -> Move code below to service
  private RegisteredClient buildClientFromRequest(CreateClientRequest clientRequest) {
    Set<String> redirectUris = new HashSet<>();
    Arrays.stream(clientRequest.getRedirectUris().split(","))
        .forEach(
            s -> {
              if (!s.trim().isBlank()) redirectUris.add(s);
            });

    Set<ClientAuthenticationMethod> authenticationMethods =
        Arrays.stream(clientRequest.getAuthenticationMethods())
            .map(ClientAuthenticationMethod::new)
            .collect(Collectors.toSet());

    Set<AuthorizationGrantType> grantTypes =
        Arrays.stream(clientRequest.getGrantTypes())
            .map(AuthorizationGrantType::new)
            .collect(Collectors.toSet());

    Set<String> scopes = Arrays.stream(clientRequest.getScopes()).collect(Collectors.toSet());

    return RegisteredClient.withId(UUID.randomUUID().toString())
        .clientName(clientRequest.getClientName())
        .clientId(clientRequest.getClientId())
        .clientSecret(clientRequest.getClientSecret())
        .postLogoutRedirectUri(clientRequest.getLogoutRedirectUri())
        .redirectUris(c -> c.addAll(redirectUris))
        .clientAuthenticationMethods(c -> c.addAll(authenticationMethods))
        .authorizationGrantTypes(c -> c.addAll(grantTypes))
        .scopes(c -> c.addAll(scopes))
        .build();
  }
}
