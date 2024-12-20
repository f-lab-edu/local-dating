package com.local_dating.user_service.application;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String loginId;
    private final String pwd;


    public CustomUserDetails(Long id, String loginId, String pwd) {
        this.id = id;
        this.loginId = loginId;
        this.pwd = pwd;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    public Long getId() {
        return id;
    }
}
