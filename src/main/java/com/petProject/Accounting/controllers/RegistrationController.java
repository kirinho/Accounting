package com.petProject.Accounting.controllers;
import com.petProject.Accounting.entities.Role;
import com.petProject.Accounting.entities.Status;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.repositories.UserRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/register")
    public String registerUser(String username, String password, String surname,
                               String name,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
                               Model model) {
        // Check if the user with the given username already exists
        User existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {

            model.addAttribute("errorMessage", "User with this username already exists.");
            return "register";
        } else {
            // User doesn't exist, create a new user
            User newUser = new User();
            newUser.setUsername(username);
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
            newUser.setSurname(surname);
            newUser.setName(name);
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setRole(Role.USER);
            newUser.setStatus(Status.ACTIVE);
            userRepository.save(newUser);
            return "redirect:/login";
        }
    }
}


