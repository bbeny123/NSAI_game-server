package pl.beny.nsai.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.beny.nsai.model.UserContext;
import pl.beny.nsai.util.RentalException;

import java.util.stream.Collectors;

public abstract class BaseController {

    private Logger logger;
    private MessageSource messageSource;
    private boolean listView;
    String viewName;
    String url;
    String redirect = "redirect:/";

    public BaseController(String viewName, String url, MessageSource messageSource) {
        this.logger = LogManager.getLogger(this.getClass());
        this.messageSource = messageSource;
        this.viewName = viewName;
        this.url = url;
    }

    public BaseController(String viewName, String url, MessageSource messageSource, boolean listView) {
        this(viewName, url, messageSource);
        this.listView = listView;
    }

    @ExceptionHandler(RentalException.class)
    public RedirectView rentalException(RentalException ex, RedirectAttributes attributes) {
        logger.warn(ex.getMessage());
        return responseInfo(ex.getUrl() != null ? ex.getUrl() : url, attributes, ex.getMessageSource());
    }

    @ExceptionHandler(BindException.class)
    public RedirectView validationException(BindException ex, RedirectAttributes attributes) {
        attributes.addFlashAttribute("info", getValidationMessage(ex.getBindingResult()));
        return new RedirectView(url);
    }

    private String getValidationMessage(BindingResult errors) {
        return errors.getFieldErrors()
                .stream()
                .map(error -> messageSource.getMessage(error.getCodes()[0], null, LocaleContextHolder.getLocale()))
                .collect(Collectors.joining("\n"));
    }

    @ExceptionHandler(Exception.class)
    public RedirectView exception(Exception ex, RedirectAttributes attributes) {
        logger.warn(ex.getMessage());
        attributes.addFlashAttribute("info", ex.getMessage());
        return new RedirectView(listView ? "/" : url);
    }

    RedirectView responseInfo(String url, RedirectAttributes attributes, String source) {
        attributes.addFlashAttribute("info", messageSource.getMessage(source, null, LocaleContextHolder.getLocale()));
        return new RedirectView(url);
    }

    String responseInfo(String viewName, Model model, String source) {
        model.addAttribute("info", messageSource.getMessage(source, null, LocaleContextHolder.getLocale()));
        return viewName;
    }

    String viewOrForwardToHome(String viewName) {
        return isAuthenticated() ? redirect : viewName;
    }

    String redirectToUrl() {
        return redirect+viewName;
    }

    UserContext getUserContext() {
        return isAuthenticated() ? (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : new UserContext();
    }

    boolean isAuthenticated() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }
}
