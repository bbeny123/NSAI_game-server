package pl.beny.nsai.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserContext implements UserDetails {

    private User user;
    private List<Role.Roles> roles;
    private List<GrantedAuthority> authorities;

    public UserContext() {
        roles = new ArrayList<>();
    }

    public UserContext(User user) {
        this.user = user;
        this.roles = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        this.authorities = AuthorityUtils.createAuthorityList(roles.stream().map(Role.Roles::getRole).toArray(String[]::new));
    }

    public User getUser() {
        return user;
    }

    public boolean isAdmin() {
        return roles.contains(Role.Roles.ADMIN);
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.isActive() && !user.getRoles().isEmpty();
    }
}
