package com.gymbuddy.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class JwtAuthentication implements Authentication {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String userName;
    private final Collection<GrantedAuthority> grantedAuthorities;

    public JwtAuthentication(final String userName,
                             final Collection<GrantedAuthority> grantedAuthorities) {
        this.userName = userName;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return getName();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) {
        // not supported
    }
}
