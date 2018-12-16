package pl.beny.nsai.dto.user;

import pl.beny.nsai.model.User;

public class UserResponse {

    private Long id;            //user id
    private String email;       //user email
    private String name;        //user name
    private String type;        //user type (values: pl.beny.nsai.model.User.Type)
    private boolean active;     //indicates if user is active

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.type = user.getType().name();
        this.active = user.isActive();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
