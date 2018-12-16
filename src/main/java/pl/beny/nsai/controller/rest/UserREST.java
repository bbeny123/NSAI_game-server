package pl.beny.nsai.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.beny.nsai.dto.user.ResendRequest;
import pl.beny.nsai.dto.user.UserRequest;
import pl.beny.nsai.service.TokenService;
import pl.beny.nsai.service.UserService;
import pl.beny.nsai.util.CaptchaUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest")
public class UserREST extends BaseREST {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public UserREST(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    //register user based on userRequest
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) throws RuntimeException {
        CaptchaUtil.verifyCaptcha(userRequest.getCaptchaResponse());
        userService.create(userRequest.getUser());
        return ok();
    }

    //activate user based on token
    @GetMapping("/register/activate")
    public ResponseEntity<?> activate(@RequestParam("token") String token) throws RuntimeException {
        userService.activate(tokenService.findByToken(token).getUser());
        return ok();
    }

    //resend email with token based on resendRequest
    @PostMapping("/register/resend")
    public ResponseEntity<?> resendToken(@Valid @RequestBody ResendRequest resendRequest) throws RuntimeException {
        userService.resendToken(userService.findByEmail(resendRequest.getEmail()));
        return ok();
    }

}
