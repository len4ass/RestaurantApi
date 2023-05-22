package ru.len4ass.api.models.session;

import jakarta.persistence.*;
import ru.len4ass.api.models.user.User;

import java.util.Date;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(columnDefinition = "text")
    private String token;

    @Column(name = "expires_at")
    private Date expiresAt;

    public Session() {

    }

    public Session(User user, String token, Date expiresAt) {
        this(0, user, token, expiresAt);
    }

    public Session(Integer id, User user, String token, Date expiresAt) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Date getExpirationDate() {
        return expiresAt;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpirationDate(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
