package com.petProject.Accounting.service;

import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserDetails(String username) {
        return userRepository.findByUsername(username);
    }
    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }
    public User getUserFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return getUserDetails(username);
    }
}

