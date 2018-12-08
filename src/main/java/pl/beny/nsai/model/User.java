package pl.beny.nsai.model;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@SequenceGenerator(sequenceName = "SEQ_USR", name = "SEQ_USR")
public class User {

    public enum Type {
        A,  //admin
        U   //standard user
    }

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Token token;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USR")
    @Column(name = "USR_ID")
    private Long id;

    @Column(name = "USR_EMAIL", length = 60, nullable = false, unique = true)
    private String email;

    @Column(name = "USR_PASSWORD", nullable = false)
    private String password;

    @Column(name = "USR_NAME", length = 120)
    private String name;

    @Column(name = "USR_TYPE", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type = Type.U;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

}