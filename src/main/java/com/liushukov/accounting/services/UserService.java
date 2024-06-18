package com.liushukov.accounting.services;

import com.liushukov.accounting.dtos.RegisterDTO;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserDetails(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserFromAuthentication(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            return getUserDetails(username);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User retrieval exception", exception);
        }
    }

    public User updateUser(Authentication authentication, RegisterDTO registerDTO){
        User user = getUserFromAuthentication(authentication);
        if (!registerDTO.surname().isEmpty()) {
            user.setSurname(registerDTO.surname());
        }
        if (!registerDTO.name().isEmpty()) {
            user.setName(registerDTO.name());
        }
        if (!registerDTO.email().isEmpty()) {
            user.setEmail(registerDTO.email());
        }
        if (!registerDTO.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(registerDTO.password()));
        }
        userRepository.save(user);
        return user;
    }
    public boolean deleteUser(Authentication authentication){
        var user = getUserFromAuthentication(authentication);
        if (user != null){
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}