package com.liushukov.accounting.services;

import com.liushukov.accounting.exceptions.CustomException;
import com.liushukov.accounting.models.EmailToken;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.EmailTokenRepository;
import com.liushukov.accounting.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private final EmailTokenRepository emailTokenRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public EmailService(EmailTokenRepository emailTokenRepository, UserRepository userRepository, JavaMailSender mailSender) {
        this.emailTokenRepository = emailTokenRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public Optional<EmailToken> getToken(String token){
        return emailTokenRepository.findByToken(token);
    }

    public String createToken(User user){
        String token = UUID.randomUUID().toString();
        var emailToken = new EmailToken(
                token,
                LocalDateTime.now().plusHours(24),
                user
        );
        emailTokenRepository.save(emailToken);
        return token;
    }

    @Async
    public void sendEmail(User user, String token) throws CustomException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildEmail(user.getName(), user.getSurname(), token), true);
            helper.setTo(user.getEmail());
            helper.setSubject("Confirm your email");
            helper.setFrom("YourEmailHere");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException("Failed to send email for: " + user.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public User confirmEmail(String token) throws CustomException {
        Optional<EmailToken> confirmToken = getToken(token);
        if (confirmToken.isEmpty()){
            throw new CustomException("Token doesn't exist!", HttpStatus.BAD_REQUEST);
        }
        if (confirmToken.get().isActivated()){
            throw new CustomException("Token was already activated", HttpStatus.CONFLICT);
        }
        if (confirmToken.get().getExpirationTime().isBefore(LocalDateTime.now())){
            throw new CustomException("Token is expired", HttpStatus.NOT_FOUND);
        }
        User user = confirmToken.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        var updateToken = confirmToken.get();
        updateToken.setActivated(true);
        emailTokenRepository.save(updateToken);
        return user;
    }

    private String buildEmail(String name, String surname, String token){
        return "<div style='font-family: Arial, sans-serif; font-size: 16px; color: #333;'>"
                + "<h2 style='color: #0066cc;'>Welcome to Our App, " + name + " " + surname + "!</h2>"
                + "<p>Thank you for registering. Below is a code to activate your account:</p>"
                + "<div style='display: inline-block; padding: 10px 20px; "
                + "margin: 20px 0; font-size: 16px; color: #ffffff; background-color: #28a745; "
                + "border-radius: 5px;'>" + token + "</div>"
                + "<p>If you have any issues, please contact our support team.</p>"
                + "<p>Best regards,<br>Your Accounting :)</p>"
                + "</div>";
    }
}
