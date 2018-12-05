package pl.beny.nsai.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.beny.nsai.model.User;

import java.util.Collection;

public class UserContext2 implements UserDetails {
    private static final long serialVersionUID = -7102193950333626170L;

    private String email;
    private String password;
    private Long userId;

    public UserContext2(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userId = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    Long getUserId() {
        return userId;
    }

}
