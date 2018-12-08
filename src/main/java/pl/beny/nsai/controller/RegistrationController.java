package pl.beny.nsai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.beny.nsai.dto.UserRequest;
import pl.beny.nsai.service.TokenService;
import pl.beny.nsai.service.UserService;
import pl.beny.nsai.util.CaptchaUtil;

import javax.validation.Valid;

@Controller
public class RegistrationController extends BaseController {

	private final UserService userService;
	private final TokenService tokenService;

	@Autowired
	public RegistrationController(UserService userService, TokenService tokenService, MessageSource messageSource) {
		super("registration", "/register", messageSource);
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@GetMapping("/register")
	public String register() {
		return viewOrForwardToHome(viewName);
	}

	@PostMapping("/register")
	public String register(Model model, @Valid UserRequest userRequest, @RequestParam("g-recaptcha-response") String captcha) throws RuntimeException {
		if (isAuthenticated()) {
			return redirect;
		}
		CaptchaUtil.verifyCaptcha(captcha);
		userService.create(userRequest.getUser());
		return responseInfo("login", model, "info.registered");
	}

	@GetMapping("/register/activate")
	public String activate(Model model, @RequestParam("token") String token) throws RuntimeException {
		if (isAuthenticated()) {
			return redirect;
		}
		userService.activate(tokenService.findByToken(token).getUser());
		return responseInfo("login", model, "info.activated");
	}

	@GetMapping("/register/resend")
	public String resend() {
		return viewOrForwardToHome("token");
	}

	@PostMapping("/register/resend")
	public String resendToken(Model model, String email) throws RuntimeException {
		if (isAuthenticated()) {
			return redirect;
		}
		userService.resendToken(userService.findByEmail(email));
		return responseInfo("login", model, "info.resend");
	}

}
