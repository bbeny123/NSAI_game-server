package pl.beny.nsai.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.beny.nsai.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class RegistrationRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String city;
    private String phone;

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

    @NotEmpty
    @Length(max = 60)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotEmpty
    @Length(max = 60)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotEmpty
    @Length(max = 60)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Length(max = 30)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser(PasswordEncoder encoder) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCity(city);
        user.setPhone(phone);
        return user;
    }
}
