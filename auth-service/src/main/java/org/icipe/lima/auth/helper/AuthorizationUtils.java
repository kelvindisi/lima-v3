package org.icipe.lima.auth.helper;

import java.util.Map;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

public class AuthorizationUtils {
    public static Map<String, String> getGrantTypes() {
        return Map.of(
                "Authorization Code", AuthorizationGrantType.AUTHORIZATION_CODE.getValue(),
                "Client Credentials", AuthorizationGrantType.CLIENT_CREDENTIALS.getValue(),
                "JWT Bearer", AuthorizationGrantType.JWT_BEARER.getValue(),
                "Refresh Token", AuthorizationGrantType.REFRESH_TOKEN.getValue(),
                "Device Code", AuthorizationGrantType.DEVICE_CODE.getValue());
    }

    public static Map<String, String> getAuthenticationMethods() {
        return Map.of(
                "JWT", ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue(),
                "Basic", ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue(),
                "Post", ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue());
    }
    public static Map<String, String> getScopes() {
        return Map.of(
                "OpenID", OidcScopes.OPENID,
                "Email", OidcScopes.EMAIL,
                "Profile", OidcScopes.PROFILE,
                "Phone", OidcScopes.PHONE
        );
    }
}
