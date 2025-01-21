package net.uway.journalApp.controller;

import net.uway.journalApp.dto.LoginDto;
import net.uway.journalApp.dto.UserDto;
import net.uway.journalApp.entity.User;
import net.uway.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
        try {
            userService.registerUser(userDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warning("Registration failed: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace for better diagnostics
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.severe("Unexpected error during registration: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace for better diagnostics
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDto loginDto) {
        logger.info("Received login request: " + loginDto);
        try {
            User user = userService.loginUser(loginDto.getEmail(), loginDto.getPassword(), passwordEncoder);
            logger.info("Login successful for user: " + user.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", String.valueOf(user.getId())); // Add user ID to response
            response.put("fullName", user.getFullName());
            response.put("email", user.getEmail());
            response.put("city", user.getCity());
            response.put("mobileNumber", user.getMobileNumber());
            response.put("referrer", user.getReferrer() != null ? user.getReferrer().getFullName() : "No referrer");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error during login: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserDetails(@PathVariable UUID userId) {
        try {
            Map<String, Object> userDetails = userService.getUserDetails(userId);
            return ResponseEntity.ok(userDetails);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for better diagnostics
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
