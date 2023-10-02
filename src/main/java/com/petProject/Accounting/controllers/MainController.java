package com.petProject.Accounting.controllers;
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


    @GetMapping("/account")
    public String account(Model model) {
        return "account";
    }

    @GetMapping("/editProfile")
    public String editProfile(Model model) {
        return "editProfile";
    }
}

