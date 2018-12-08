package pl.beny.nsai.dto.user;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ResendRequest {

    private String email;

    @NotEmpty
    @Email
    @Length(max = 60)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
