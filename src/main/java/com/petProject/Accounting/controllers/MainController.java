package com.petProject.Accounting.controllers;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;

@Controller
public class MainController {
    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String greeting(Model model, Authentication authen) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !"anonymousUser".equals(authentication.getPrincipal());

        if(isAuthenticated){
            User user = userService.getUserFromAuthentication(authen);
            if (user.getPhoto() != null) {
                String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
                model.addAttribute("userPhoto", base64Photo);
            } else {
                model.addAttribute("userPhoto", "images/userAva.png");
            }
            return "greeting-authenticated";
        }
        else{
            return "greeting";
        }

//        return isAuthenticated ? "greeting-authenticated" : "greeting";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }


}

