package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.RegisterDTO;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication){
        logger.info("entrance in getting user's info method");
        User user = userService.getUserFromAuthentication(authentication);
        logger.info("got response");
        return ResponseEntity.ok(user);
    }
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(Authentication authentication, @RequestBody RegisterDTO registerDTO){
        logger.info("entrance in update user method");
        var updatedUser = userService.updateUser(authentication, registerDTO);
        logger.info("got response");
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Authentication authentication){
        logger.info("entrance in delete user method");
        var deletedUser = userService.deleteUser(authentication);
        logger.info("got response");
        return (deletedUser)
                ? ResponseEntity.ok("User successfully deleted!")
                : ResponseEntity.ok("User didn't deleted for some reason :(");
    }

}
