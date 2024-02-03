package org.icipe.lima.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.icipe.lima.auth.helper.FileHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  public static final Path CERT_FOLDER_PATH = Paths.get("cert", "rsa");
  private final PasswordEncoder passwordEncoder;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());
    http.exceptionHandling(
        (exception) ->
            exception.defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
    //        to enable resource access for authenticated users eg -> user details
    http.oauth2ResourceServer((resource) -> resource.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(Customizer.withDefaults())
        .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
    return http.build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    var client =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientSecret(passwordEncoder.encode("password"))
            .clientName("Test Client")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .scope(OidcScopes.OPENID)
            .redirectUri("https://example.com/auth")
            .postLogoutRedirectUri("https://example.com/auth/login")
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofHours(24))
                    .refreshTokenTimeToLive(Duration.ofDays(2))
                    .build())
            .build();
    return new InMemoryRegisteredClientRepository(client);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    var user =
        User.withUsername("admin")
            .password(passwordEncoder.encode("password"))
            .authorities("read")
            .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    KeyPair keyPair = generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    RSAKey rsaKey =
        new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return new ImmutableJWKSet<>(jwkSet);
  }

  private KeyPair generateKeyPair() {
    KeyPair keyPair;

    try {
      keyPair = getFileKeyPairIfExists();
      if (keyPair == null) {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
        saveKeyPairToFileSystem(keyPair);
      }
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException(ex);
    }

    return keyPair;
  }

  private void saveKeyPairToFileSystem(KeyPair keyPair) {
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

    FileHelper.createFoldersIfNotExists(CERT_FOLDER_PATH);

    FileHelper.writeFile(CERT_FOLDER_PATH.resolve("private.key"), privateKey.getEncoded());
    FileHelper.writeFile(CERT_FOLDER_PATH.resolve("public.pub"), publicKey.getEncoded());
  }

  private KeyPair getFileKeyPairIfExists() {
    try {
      Path privatePath = CERT_FOLDER_PATH.resolve("private.key");
      Path publicPath = CERT_FOLDER_PATH.resolve("public.pub");

      boolean privateExist = FileHelper.fileExists(privatePath);
      boolean publicExist = FileHelper.fileExists(publicPath);

      if (privateExist && publicExist) {
        byte[] privateKeyBytes = FileHelper.readFile(privatePath);
        byte[] publicKeyBytes = FileHelper.readFile(publicPath);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        //        if no data in key files -> we don't build the keyPair.
        if (privateKeyBytes == null || publicKeyBytes == null) return null;

        EncodedKeySpec privateEncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateEncodedKeySpec);

        EncodedKeySpec publicEncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicEncodedKeySpec);

        return new KeyPair(publicKey, privateKey);
      }
      return null;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }

  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
  }
}