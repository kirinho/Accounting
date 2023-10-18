package com.petProject.Accounting.controllers;
import com.petProject.Accounting.entities.UserAuthentication;
import com.petProject.Accounting.repositories.UserRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(String username, String password, String surname,
                               String name,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
                               Model model) {
        // Check if the user with the given username already exists
        UserAuthentication existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {
            // User with the same username already exists, handle the error (e.g., display a message)
            System.out.println("User exists!");
            model.addAttribute("errorMessage", "User with this username already exists.");
            return "register";
        } else {
            // User doesn't exist, create a new user
            UserAuthentication newUser = new UserAuthentication();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setSurname(surname);
            newUser.setName(name);
            newUser.setDateOfBirth(dateOfBirth);
            userRepository.save(newUser);
//            System.out.println(newUser.getUsername());
//            System.out.println(newUser.getPassword());
//            System.out.println(newUser.getSurname());
//            System.out.println(newUser.getName());
//            System.out.println(newUser.getDateOfBirth());

            return "redirect:/account";
        }
    }
}


