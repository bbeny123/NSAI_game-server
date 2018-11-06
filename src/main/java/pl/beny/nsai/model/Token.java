package pl.beny.nsai.model;

import javax.persistence.*;

@Entity
@Table(name = "TOKENS")
public class Token {

    @OneToOne
    @MapsId
    @JoinColumn(name = "TKN_ID")
    private User user;

    @Id
    private Long id;

    @Column(name = "TKN_TOKEN", nullable = false, unique = true)
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
