package pl.beny.nsai.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginWeb extends BaseWeb {

    @Autowired
    public LoginWeb(MessageSource messageSource) {
        super("login", "/login", messageSource);
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        if (isAuthenticated()) {
            return redirect;
        }
        if (request.getParameter("logout") != null) {
            return responseInfo(viewName, model, "info.logout");
        }
        return viewName;
    }

    @GetMapping("/login/failure")
    public String failure(Model model) {
        if (isAuthenticated()) {
            return redirect;
        }
        return responseInfo(viewName, model, "info.invalid.credentials");
    }

}
