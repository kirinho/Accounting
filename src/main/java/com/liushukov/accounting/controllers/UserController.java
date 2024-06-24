package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.RegisterDTO;
import com.liushukov.accounting.exceptions.CustomException;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.services.EmailService;
import com.liushukov.accounting.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me(Authentication authentication){
        try {
            logger.info("entrance in getting user's info method");
            User user = userService.getUserFromAuthentication(authentication);
            logger.info("got response in user's info method");
            return ResponseEntity.ok(user);
        } catch (Exception exception){
            logger.error("unexpected error in user's info method: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(Authentication authentication, @RequestBody RegisterDTO registerDTO){
        try {
            logger.info("entrance in update user method");
            var updatedUser = userService.updateUser(authentication, registerDTO);
            logger.info("got response in update user method");
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (Exception exception){
            logger.error("unexpected error in update user method: " + exception);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Authentication authentication){
        try {
            logger.info("entrance in delete user method");
            var deletedUser = userService.deleteUser(authentication);
            logger.info("got response in delete user method");
            return (deletedUser)
                    ? ResponseEntity.status(HttpStatus.OK).body("User successfully deleted!")
                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
        } catch (Exception exception){
            logger.error("unexpected error in delete user method: " + exception);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<Object> confirmEmail(@RequestParam(value = "token") String token) throws CustomException {
        try {
            logger.info("verify email and get user in confirm email method");
            var user = emailService.confirmEmail(token);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception exception){
            if (exception instanceof CustomException){
                logger.error("custom exception in confirm email method: " + exception);
                throw exception;
            }
            logger.error("unexpected error in delete user method: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
