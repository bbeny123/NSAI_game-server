package pl.beny.nsai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.beny.nsai.service.UserService;

@Component
public class SecuredRequestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    private static String JWT_SIGNING_KEY = "";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws GamesException {
//        SecurityContextHolder.setContext();
//        UserContext ctx = UserContextHolder.getCurrentProcessContext();
//        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (auth == null || auth.isEmpty() || !auth.startsWith("Bearer ")) {
//            throw new GamesException(GamesException.GamesErrors.UNAUTHORIZED);
//        }
//
//        try {
//            parseJwt(auth.replace("Bearer ", ""), ctx);
//        } catch (Exception e) {
//            throw new GamesException(GamesException.GamesErrors.UNAUTHORIZED);
//        }
//
//        return true;
//    }
//
//    protected void parseJwt(String jwt, UserContext ctx) throws RuntimeException {
//        Jwt token = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(JWT_SIGNING_KEY.getBytes())).parse(jwt);
//
//        DefaultClaims payload = (DefaultClaims) token.getBody();
//        DefaultJwsHeader headers = (DefaultJwsHeader) token.getHeader();
//
//        if (payload.get("userId") == null) {
//            throw new MalformedJwtException("Unauthorized");
//        }
//
//        ctx.setUserId(payload.get("userId", Long.class));
//    }

}
