package com.ms.chat.application.services;

import java.util.stream.Collectors;

import com.ms.chat.application.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ms.chat.application.Repository.UserRepository;

@Service
public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {

        // Fetch the user by username
        User user = userRepository.findByuserName(userName);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user with username: " + userName);
        }

        // Map the user to CustomUserDetails with additional information
        return new CustomUserDetails(
                user.getUserName(),
                user.getPassword(),
                user.getId(), // Pass userId
                user.getEmail(), // Pass email
                user.getRole().stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList())
        );
    }
}
