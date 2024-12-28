package net.uway.journalApp.services;

import net.uway.journalApp.dto.UserDto;
import net.uway.journalApp.entity.User;
import net.uway.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public User registerUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        logger.info("Registering user: " + userDto);
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setCity(userDto.getCity());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    public User loginUser(String email, String password, PasswordEncoder passwordEncoder) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User found with email: " + email);
            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Authentication successful for user: " + email);
                return user;
            } else {
                logger.warning("Invalid password for user: " + email);
            }
        } else {
            logger.warning("No user found with email: " + email);
        }
        throw new RuntimeException("Invalid credentials");
    }


    public void sendReferralEmail(String referrerEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(referrerEmail);
        message.setSubject("Join MyApp and Get Started!");
        message.setText("Hello, \n\nYou have been invited to join MyApp. Click the link below to register:\n\nhttp://localhost:8080/registration.html\n\nBest Regards,\nMyApp Team");
        mailSender.send(message);
    }
}
