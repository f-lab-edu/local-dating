package com.local_dating.user_service.application;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String loginId;
    private final String pwd;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String loginId, String pwd, List<GrantedAuthority> authorities) {
        this.id = id;
        this.loginId = loginId;
        this.pwd = pwd;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
