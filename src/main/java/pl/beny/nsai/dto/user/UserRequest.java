package pl.beny.nsai.dto.user;

import org.hibernate.validator.constraints.Length;
import pl.beny.nsai.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

//user registration request
public class UserRequest {

    private String email;               //user email
    private String password;            //user password
    private String name;                //user name
    private String captchaResponse;     //registration captcha widget result

    @NotEmpty
    @Email
    @Length(max = 60)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty
    @Length(max = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(max = 120)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty
    public String getCaptchaResponse() {
        return captchaResponse;
    }

    public void setCaptchaResponse(String captchaResponse) {
        this.captchaResponse = captchaResponse;
    }

    public User getUser() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        return user;
    }
}
