package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.LoginDTO;
import com.liushukov.accounting.dtos.RegisterDTO;
import com.liushukov.accounting.exceptions.AuthenticationException;
import com.liushukov.accounting.exceptions.RegistrationException;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.responses.LoginResponse;
import com.liushukov.accounting.services.AuthenticationService;
import com.liushukov.accounting.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/api")
@RestController
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterDTO registerUserDto) {
        logger.info("entrance in register method");
        try {
            User registeredUser = authenticationService.signup(registerUserDto);
            logger.info("registered user and received response");
            return ResponseEntity.ok(registeredUser);
        } catch (Exception exception) {
            logger.error("Error during registration");
            throw new RegistrationException("Something went wrong during :( Try again!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginDTO loginUserDto) {
        logger.info("entrance in authenticate method");
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            logger.info("authenticated user");
            String jwtToken = jwtService.generateToken(authenticatedUser);
            logger.info("generated jwt for authenticated user");
            LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
            logger.info("created response");
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException ex) {
            logger.error("Error during authentication - bad credentials");
            throw new AuthenticationException("Incorrect login or password");
        }
    }
}
