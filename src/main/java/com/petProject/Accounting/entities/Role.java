package com.petProject.Accounting.entities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Collections.singleton("ROLE_USER")),
    ADMIN(Collections.singleton("ROLE_ADMIN"));

    private final Set<SimpleGrantedAuthority> authorities;

    Role(Set<String> roles) {
        this.authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }
}


