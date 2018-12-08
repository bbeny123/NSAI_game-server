package pl.beny.nsai.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.beny.nsai.dto.user.CaptchaResponse;

import java.util.Objects;

@Component
public class CaptchaUtil {

    private static RestTemplate restTemplate;
    private static String url;
    private static String site;
    private static String secret;
    private static boolean captchaEnabled;

    @Autowired
    private CaptchaUtil(RestTemplate restTemplate, @Value("${captcha.url:null}") String url, @Value("${captcha.site:null}") String site, @Value("${captcha.secret:null}") String secret, @Value("${captcha.enable:false}") boolean captchaEnabled) {
        CaptchaUtil.restTemplate = restTemplate;
        CaptchaUtil.url = url;
        CaptchaUtil.site = site;
        CaptchaUtil.secret = secret;
        CaptchaUtil.captchaEnabled = captchaEnabled;
    }

    public static void verifyCaptcha(String response) throws GamesException {
        try {
            if (captchaEnabled && !Objects.requireNonNull(restTemplate.getForEntity(url, CaptchaResponse.class, secret, response).getBody()).isSuccess()) {
                throw new GamesException(GamesException.GamesErrors.CAPTCHA_ERROR);
            }
        } catch (GamesException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GamesException(GamesException.GamesErrors.INTERNAL_SERVER_ERROR);
        }
    }

    public static String getSite() {
        return site;
    }
}