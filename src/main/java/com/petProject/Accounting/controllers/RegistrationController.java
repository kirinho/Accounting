package com.petProject.Accounting.controllers;
import com.petProject.Accounting.entities.UserAuthentication;
import com.petProject.Accounting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(String username, String password) {
        // Check if the user with the given username already exists
        UserAuthentication existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {
            // User with the same username already exists, handle the error (e.g., display a message)
            System.out.println("User exists!");
            return "redirect:/register?error=exists";
        } else {
            // User doesn't exist, create a new user
            UserAuthentication newUser = new UserAuthentication();
            newUser.setUsername(username);
            newUser.setPassword(password);
            userRepository.save(newUser);
            System.out.println(newUser.getUsername());
            System.out.println(newUser.getPassword());

            // Redirect to a success page or login page
            return "redirect:/account";
        }
    }
}


