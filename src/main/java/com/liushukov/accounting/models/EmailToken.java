package com.liushukov.accounting.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token", nullable = false, unique = true)
    private String token;
    @Column(name = "expirationTime", nullable = false)
    private LocalDateTime expirationTime;
    @Column(name = "activated", nullable = false)
    private boolean activated;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public EmailToken(){}

    public EmailToken(String token, LocalDateTime expirationTime, User user) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.activated = false;
        this.user = user;
    }

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

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
