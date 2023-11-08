package com.petProject.Accounting.service;

import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserDetails(String username) {
        return userRepository.findByUsername(username);
    }
}

