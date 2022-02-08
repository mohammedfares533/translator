package com.cerebra.translator.dto;

import com.cerebra.translator.model.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean active;
    private boolean isLocked;
    private boolean isExpired;
    private boolean isEnabled;
    private List<GrantedAuthority> grantedAuthorities;

    public CustomUserDetails(SystemUser user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.isLocked = user.isLocked();
        this.isExpired = user.isExpired();
        this.isEnabled = user.isEnabled();

        String authority = user.getRole().name();

        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authority);
    }

    public CustomUserDetails(String username, String authorities) {
        this.username = username;
        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
    }

    public CustomUserDetails() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
