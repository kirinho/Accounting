package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.LoginDTO;
import com.liushukov.accounting.dtos.RegisterDTO;
import com.liushukov.accounting.exceptions.CustomException;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.UserRepository;
import com.liushukov.accounting.responses.LoginResponse;
import com.liushukov.accounting.services.AuthenticationService;
import com.liushukov.accounting.services.EmailService;
import com.liushukov.accounting.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api")
@RestController
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserRepository userRepository, EmailService emailService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterDTO registerUserDto) throws CustomException {
        logger.info("entrance in register method");
        try {
            var existedUsername = userRepository.findByEmail(registerUserDto.email()).orElse(null);
            if (existedUsername == null) {
                User registeredUser = authenticationService.signup(registerUserDto);
                logger.info("registered user and received response");
                var token = emailService.createToken(registeredUser);
                logger.info("generated token");
                emailService.sendEmail(registeredUser, token);
                logger.info("sent email for verification user's account");
                return ResponseEntity.status(HttpStatus.OK).body(registeredUser);
            } throw new CustomException("User is already registered with that email!", HttpStatus.CONFLICT);
        } catch (Exception exception) {
            if (exception instanceof CustomException customException) {
                logger.error("Error during registration - user already exists with that email");
                throw customException;
            }
            logger.error("Unexpected error during registration: " + exception);
            throw new CustomException("Something went wrong during registration :( Try again!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginDTO loginUserDto) throws CustomException {
        logger.info("entrance in authenticate method");
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            logger.info("authenticated user");
            String jwtToken = jwtService.generateToken(authenticatedUser);
            logger.info("generated jwt for authenticated user");
            LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
            logger.info("created response");
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } catch (Exception ex) {
            if (ex instanceof BadCredentialsException){
                logger.error("Error during authentication - bad credentials");
                throw new CustomException("Incorrect login or password", HttpStatus.UNAUTHORIZED);
            } else if (ex instanceof DisabledException) {
                logger.error("Error during authentication - user doesn't verify email");
                throw new CustomException("You should verify your email", HttpStatus.UNAUTHORIZED);
            }
            logger.error("An unexpected error occurred during authentication: " + ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during authentication");
        }
    }
}
