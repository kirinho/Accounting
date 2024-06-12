package com.liushukov.accounting.services;

import com.liushukov.accounting.dtos.RegisterDTO;
import com.liushukov.accounting.exceptions.UserRetrievalException;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new UserRetrievalException("Error retrieving user from authentication");
        }
    }

    public User updateUser(Authentication authentication, RegisterDTO registerDTO){
        User user = getUserFromAuthentication(authentication);
        if (!registerDTO.getSurname().isEmpty()) {
            user.setSurname(registerDTO.getSurname());
        }
        if (!registerDTO.getName().isEmpty()) {
            user.setName(registerDTO.getName());
        }
        if (!registerDTO.getEmail().isEmpty()) {
            user.setEmail(registerDTO.getEmail());
        }
        if (!registerDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
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
