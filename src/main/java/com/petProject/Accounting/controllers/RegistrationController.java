package com.petProject.Accounting.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {

    @PostMapping("/register")
    public String registerUser(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        return "redirect:/login";
    }
}

