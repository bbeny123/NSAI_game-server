package pl.beny.nsai.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.beny.nsai.dto.CaptchaResponse;

@Component
public class CaptchaUtil {

    @Value("${captcha.url:null}")
    private String url;

    @Value("${captcha.site:null}")
    private String site;

    @Value("${captcha.secret:null}")
    private String secret;

    private final RestTemplate restTemplate;

    @Autowired
    private CaptchaUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean checkCaptcha(String response) {
        return restTemplate.getForEntity(url, CaptchaResponse.class, secret, response).getBody().isSuccess();
    }

    public String getSite() {
        return site;
    }
}
