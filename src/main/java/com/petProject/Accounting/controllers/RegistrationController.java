package com.petProject.Accounting.controllers;
import com.petProject.Accounting.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute("user") User user) {
        // Implement your registration logic, including password hashing and validation

        // For simplicity, let's assume you've already hashed the password.
        userRepository.save(user); // Save the user to the database

        return "redirect:/login"; // Redirect to a success page or login page
    }
}



