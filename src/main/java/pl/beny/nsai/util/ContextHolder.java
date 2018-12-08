package pl.beny.nsai.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import pl.beny.nsai.model.UserContext;

public class ContextHolder {

    public static UserContext getUserContext() {
        return isAuthenticated() ? (UserContext) ((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication()).getUserAuthentication() : new UserContext();
    }

    public static boolean isAuthenticated() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

}
