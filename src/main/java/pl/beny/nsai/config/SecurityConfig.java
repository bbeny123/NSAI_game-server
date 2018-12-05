package pl.beny.nsai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.beny.nsai.model.Role;
import pl.beny.nsai.service.UserContextService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private UserContextService userService;
    private SecuredRequestInterceptor securedRequestInterceptor;

    @Autowired
    public SecurityConfig(UserContextService userService, SecuredRequestInterceptor securedRequestInterceptor) {
        this.userService = userService;
        this.securedRequestInterceptor = securedRequestInterceptor;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/static/**", "/", "/login/**", "/register/**", "/rest/**").permitAll()
                .antMatchers("/users/**").hasAuthority(Role.Roles.ADMIN.getRole())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/")
                .failureUrl("/login/failure")
                .and()
                .rememberMe()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("remember-me");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    public void addInterceptors(@NonNull InterceptorRegistry registry) {
//        registry.addInterceptor(securedRequestInterceptor).addPathPatterns("/rest/**").excludePathPatterns("/rest/register/**");
//    }

}
