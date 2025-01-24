package org.uway.NarayanSena.controller;

import org.uway.NarayanSena.dto.LoginDto;
import org.uway.NarayanSena.dto.UserDto;
import org.uway.NarayanSena.dto.UserDetailsDto;
import org.uway.NarayanSena.entity.User;
import org.uway.NarayanSena.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDto userDto) {
        logger.info("Received registration request for email: " + userDto.getEmail());
        userService.registerUser(userDto); // Let the GlobalExceptionHandler handle exceptions
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDto loginDto) {
        logger.info("Received login request: " + loginDto);
        User user = userService.loginUser(loginDto.getEmail(), loginDto.getPassword(), passwordEncoder);
        logger.info("Login successful for user: " + user.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("userId", String.valueOf(user.getId()));
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("city", user.getCity());
        response.put("mobileNumber", user.getMobileNumber());
        response.put("referrer", user.getReferrer() != null ? user.getReferrer().getFullName() : "No referrer");

        response.put("referralId", user.getReferralId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable long userId) {
        UserDetailsDto userDetails = userService.getUserDetails(userId);
        if (!userDetails.isPaymentComplete()) {
            userDetails.setPaymentStatus("Payment Pending. Please complete your payment.");
        } else {
            userDetails.setPaymentStatus("Payment Done");
        }
        return ResponseEntity.ok(userDetails);
    }
}