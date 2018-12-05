package pl.beny.nsai.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class TokenServices extends DefaultTokenServices {

    private AuthenticationManager authManager;

    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication auth = super.loadAuthentication(accessTokenValue);
        try {
            this.authManager.authenticate(new PreAuthenticatedAuthenticationToken(auth.getUserAuthentication(), "", auth.getAuthorities()));
        } catch (Exception e) {
            throw new InvalidTokenException(e.getMessage());
        }
        return auth;
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        this.authManager = authenticationManager;
    }

}
