// CustomUserDetails.java
package com.ms.chat.application.services;  // or com.ms.chat.application.security

import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;


@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    
    
    private String userName;
    private String password;
    private ObjectId userId;  // New field for userId
    private String email; // New field for email
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor to initialize fields
    public CustomUserDetails(String userName, String password, ObjectId userId, String email,
                             Collection<? extends GrantedAuthority> authorities) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.email = email;
        this.authorities = authorities;
    }

    // Getter methods
    public ObjectId getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
