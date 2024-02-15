package org.icipe.lima.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.icipe.lima.auth.validation.ValidAuthenticationMethod;
import org.icipe.lima.auth.validation.ValidClientId;
import org.icipe.lima.auth.validation.ValidGrantType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateClientRequest {
  @NotBlank @ValidClientId private String clientId;
  @NotBlank private String clientSecret;
  @NotBlank private String clientName;
  @NotBlank private String redirectUris;
  @NotBlank private String logoutRedirectUri;
  @NotEmpty @ValidAuthenticationMethod private String[] authenticationMethods;
  @NotEmpty @ValidGrantType private String[] grantTypes;
  @NotEmpty private String[] scopes;
}
