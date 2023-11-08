package com.petProject.Accounting.controllers;
import com.petProject.Accounting.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("greetings", "Main page");
        return "greeting";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }




    @GetMapping("/editProfile")
    public String editProfile(Model model) {
        return "editProfile";
    }
}

