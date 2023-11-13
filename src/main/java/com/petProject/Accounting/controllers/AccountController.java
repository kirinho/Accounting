package com.petProject.Accounting.controllers;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;


@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String account(Authentication authentication, Model model) {
        User user = userService.getUserFromAuthentication(authentication);

        model.addAttribute("username", user.getUsername());
        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("surname", user.getSurname());
            model.addAttribute("dateOfBirth", user.getDateOfBirth());
            if(user.getQuotes() != null){
                model.addAttribute("quotes", user.getQuotes());
            }
            else{
                model.addAttribute("quotes", "Input some quotes if you wanna");
            }
            if (user.getPhoto() != null) {
                String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
                model.addAttribute("userPhoto", base64Photo);
            } else {
                model.addAttribute("userPhoto", "images/userAva.png");
            }

        }

        return "account";
    }
}

