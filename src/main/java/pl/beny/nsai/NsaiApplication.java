package pl.beny.nsai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NsaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NsaiApplication.class, args);
    }

    @Bean
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
