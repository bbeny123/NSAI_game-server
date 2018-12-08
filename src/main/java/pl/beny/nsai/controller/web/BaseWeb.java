package pl.beny.nsai.controller.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.beny.nsai.model.UserContext;
import pl.beny.nsai.util.ContextHolder;
import pl.beny.nsai.util.GamesException;

public abstract class BaseWeb {

    final String viewName;
    final String url;
    final String redirect = "redirect:/";
    private final Logger logger;
    private final MessageSource messageSource;
    private boolean listView;

    public BaseWeb(String viewName, String url, MessageSource messageSource) {
        this.logger = LogManager.getLogger(this.getClass());
        this.messageSource = messageSource;
        this.viewName = viewName;
        this.url = url;
    }

    public BaseWeb(String viewName, String url, MessageSource messageSource, boolean listView) {
        this(viewName, url, messageSource);
        this.listView = listView;
    }

    @ExceptionHandler(GamesException.class)
    public RedirectView amaException(GamesException ex, RedirectAttributes attributes) {
        logger.warn(ex.getMessage());
        attributes.addFlashAttribute("info", ex.toString());
        return new RedirectView(url);
    }

    @ExceptionHandler(BindException.class)
    public RedirectView validationException(BindException ex, RedirectAttributes attributes) {
        attributes.addFlashAttribute("info", ex.getMessage());
        return new RedirectView(url);
    }

    @ExceptionHandler(Exception.class)
    public RedirectView exception(Exception ex, RedirectAttributes attributes) {
        logger.warn(ex.getMessage());
        attributes.addFlashAttribute("info", ex.getMessage());
        return new RedirectView(listView ? "/" : url);
    }

    protected String responseInfo(String viewName, Model model, String source) {
        model.addAttribute("info", messageSource.getMessage(source, null, LocaleContextHolder.getLocale()));
        return viewName;
    }

    protected String viewOrForwardToHome(String viewName) {
        return isAuthenticated() ? redirect : viewName;
    }

    protected String redirectToUrl() {
        return redirect + viewName;
    }

    protected UserContext getUserContext() {
        return isAuthenticated() ? (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : new UserContext();
    }

    protected boolean isAuthenticated() {
        return ContextHolder.isAuthenticated();
    }

}
