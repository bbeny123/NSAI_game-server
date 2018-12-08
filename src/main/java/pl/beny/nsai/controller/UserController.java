package pl.beny.nsai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.beny.nsai.dto.UserResponse;
import pl.beny.nsai.service.UserService;

import java.util.stream.Collectors;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, MessageSource messageSource) {
        super("users", "/users", messageSource, true);
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(Model model) throws RuntimeException {
        model.addAttribute("userId", getUserContext().getUserId());
        model.addAttribute("users", userService.findAllAdmin(getUserContext()).stream().map(UserResponse::new).collect(Collectors.toList()));
        return viewName;
    }

    @PostMapping("/users/{userId}/activate")
    public String activate(@PathVariable("userId") Long userId) throws RuntimeException {
        userService.activate(getUserContext(), userId);
        return redirectToUrl();
    }

}
