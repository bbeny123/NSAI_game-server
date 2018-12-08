package pl.beny.nsai.dto;

import pl.beny.nsai.model.User;

public class UserResponse extends UserListResponse {

    private String type;
    private boolean active;

    public UserResponse(User user) {
        super(user);
        this.type = user.getType().name();
        this.active = user.isActive();
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
