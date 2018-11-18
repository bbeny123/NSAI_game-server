package pl.beny.nsai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.beny.nsai.dto.RegistrationRequest;
import pl.beny.nsai.dto.ResendRequest;
import pl.beny.nsai.service.TokenService;
import pl.beny.nsai.service.UserService;
import pl.beny.nsai.util.CaptchaUtil;
import pl.beny.nsai.util.GamesException;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest")
public class RegistrationRESTController extends AbstractRESTController {

    @Value("${captcha.enable:false}")
    private boolean captcha;

    private UserService userService;
    private TokenService tokenService;
    private PasswordEncoder encoder;
    private CaptchaUtil captchaUtil;

    @Autowired
    public RegistrationRESTController(UserService userService, TokenService tokenService, PasswordEncoder encoder, CaptchaUtil captchaUtil) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.encoder = encoder;
        this.captchaUtil = captchaUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest userRequest) throws Exception {
        if (captcha && !captchaUtil.checkCaptcha(userRequest.getCaptchaResponse())) throw new GamesException(GamesException.GamesErrors.CAPTCHA_ERROR);
        userService.create(userRequest.getUser(encoder));
        return ok();
    }

    @GetMapping("/register/activate")
    public ResponseEntity<?> activate(@RequestParam("token") String token) throws Exception {
        userService.activate(tokenService.findByToken(token).getUser());
        return ok();
    }

    @PostMapping("/register/resend")
    public ResponseEntity<?> resendToken(@Valid @RequestBody ResendRequest resendRequest) throws Exception {
        userService.resendToken(userService.findByEmail(resendRequest.getEmail()));
        return ok();
    }

}
