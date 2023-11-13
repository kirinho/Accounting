package com.petProject.Accounting.controllers;


import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Base64;

import org.springframework.transaction.annotation.Transactional;

@Controller
public class EditProfileController {

    private final UserService userService;

    @Autowired
    public EditProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/editProfile")
    public String showEditProfileForm(Authentication authentication, Model model) {
        User user = userService.getUserFromAuthentication(authentication);
        if (user.getPhoto() != null) {
            String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
            model.addAttribute("userPhoto", base64Photo);
        } else {
            model.addAttribute("userPhoto", "images/userAva.png");
        }

        return "editProfile";
    }

    @Transactional
    @PostMapping("/processEditProfile")
    public String processEditProfile(Authentication authentication, Model model,
                                     @RequestParam("profilePhoto") MultipartFile profilePhoto,
                                     @RequestParam("surname") String surname,
                                     @RequestParam("name") String name,
                                     @RequestParam("birthdayDate") String birthdayDate,
                                     @RequestParam("quotes") String quotes) {

        User user = userService.getUserFromAuthentication(authentication);

        model.addAttribute("username", user.getUsername());

        if (surname != null && !surname.isEmpty()) {
            user.setSurname(surname);
        }

        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }

        if (birthdayDate != null && !birthdayDate.isEmpty()) {
            try {
                user.setDateOfBirth(LocalDate.parse(birthdayDate));
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }

        if (quotes != null && !quotes.isEmpty()) {
            user.setQuotes(quotes);
        }

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                byte[] photoBytes = profilePhoto.getBytes();
                user.setPhoto(photoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userService.saveOrUpdateUser(user);
        return "redirect:/account";
    }
}



