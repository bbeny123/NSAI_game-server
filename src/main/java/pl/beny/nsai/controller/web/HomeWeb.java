package pl.beny.nsai.controller.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class HomeWeb {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String redirect() {
        return "redirect:/";
    }

}
