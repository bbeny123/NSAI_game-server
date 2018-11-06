package pl.beny.nsai.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@SequenceGenerator(sequenceName = "SEQ_USR", name = "SEQ_USR")
@NamedEntityGraph(name = User.EntityGraphs.WITH_ROLES, attributeNodes = @NamedAttributeNode("roles"))
public class User {

    public interface EntityGraphs {
        String WITH_ROLES = "User.WITH_ROLES";
    }

    public interface Action {
        String GRANT = "grant";
        String REVOKE = "revoke";
    }

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Token token;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "URL_USR_ID"), inverseJoinColumns = @JoinColumn(name = "URL_ROL_ID"))
    private Set<Role> roles;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USR")
    @Column(name = "USR_ID")
    private Long id;

    @Column(name = "USR_EMAIL", length = 60, nullable = false, unique = true)
    private String email;

    @Column(name = "USR_PASSWORD", nullable = false)
    private String password;

    @Column(name = "USR_FIRST_NAME", length = 60, nullable = false)
    private String firstName;

    @Column(name = "USR_LAST_NAME", length = 60, nullable = false)
    private String lastName;

    @Column(name = "USR_CITY", length = 60, nullable = false)
    private String city;

    @Column(name = "USR_PHONE", length = 30)
    private String phone;

    @Column(name = "USR_ACTIVE")
    private boolean active;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getIdFullName() {
        return id + ": " + firstName + " " + lastName;
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

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setToken(String token) {
        if (this.token == null) {
            this.token = new Token();
            this.token.setUser(this);
        }
        this.token.setToken(token);
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
