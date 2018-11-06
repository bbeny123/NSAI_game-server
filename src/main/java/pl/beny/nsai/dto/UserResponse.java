package pl.beny.nsai.dto;

import pl.beny.nsai.model.Role;
import pl.beny.nsai.model.User;

public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String city;
    private String phone;
    private boolean active;
    private boolean admin;
    private boolean user;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getFullName();
        this.city = user.getCity();
        this.phone = user.getPhone();
        this.active = user.isActive();
        this.admin = user.getRoles().stream().anyMatch(role -> Role.Roles.ADMIN.equals(role.getRole()));
        this.user = user.getRoles().stream().anyMatch(role -> Role.Roles.USER.equals(role.getRole()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }
}
