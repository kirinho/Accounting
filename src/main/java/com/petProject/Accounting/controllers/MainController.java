package com.petProject.Accounting.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String greet(Model model) {
        model.addAttribute("greetings", "Main page");
        return "greeting";
    }
}

