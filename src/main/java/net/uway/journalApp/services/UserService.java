package net.uway.journalApp.services;

import jakarta.transaction.Transactional;
import net.uway.journalApp.dto.UserDto;
import net.uway.journalApp.entity.User;
import net.uway.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.*;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private static final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserDto userDto) {


        // Check if the email already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists. Please use a different email.");
        }

        // Create a new user and set properties
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setCity(userDto.getCity());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Generate a unique referral ID
        String referralId = generateReferralId();
        user.setReferralId(referralId);

        // If the user was referred by someone, set the referrer
        if (userDto.getReferrerId() != null && !userDto.getReferrerId().isEmpty()) {
            User referrer = userRepository.findByReferralId(userDto.getReferrerId())
                    .orElseThrow(() -> new RuntimeException("Invalid referrer ID"));
            user.setReferrer(referrer);
            user.setReferrerId(referrer.getReferralId());
            referrer.getReferredUsers().add(user);
        }

        return userRepository.save(user);
    }

    private String generateReferralId() {
        String referralId;
        do {
            int length = random.nextInt(3) + 3;
            StringBuilder referralIdBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                referralIdBuilder.append(random.nextInt(10));
            }
            referralId = referralIdBuilder.toString();
        } while (userRepository.existsByReferralId(referralId));
        return referralId;
    }

    public Map<String, Object> getUserDetails(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("fullName", user.getFullName());
        userDetails.put("email", user.getEmail());
        userDetails.put("city", user.getCity());
        userDetails.put("mobileNumber", user.getMobileNumber());
        userDetails.put("referrer", user.getReferrer() != null ? user.getReferrer().getFullName() : "No referrer");
        userDetails.put("paymentStatus", user.isPaymentComplete() ? "Payment Done" : "Payment Pending");
        userDetails.put("referralTree", getReferralTree(user));
        return userDetails;
    }

    private List<Map<String, Object>> getReferralTree(User user) {
        List<Map<String, Object>> referralTree = new ArrayList<>();
        for (User referral : user.getReferredUsers()) {
            Map<String, Object> referralDetails = new HashMap<>();
            referralDetails.put("fullName", referral.getFullName());
            referralDetails.put("email", referral.getEmail());
            referralDetails.put("city", referral.getCity());
            referralDetails.put("mobileNumber", referral.getMobileNumber());
            referralDetails.put("referrer", referral.getReferrer() != null ? referral.getReferrer().getFullName() : "No referrer");
            referralDetails.put("paymentStatus", referral.isPaymentComplete() ? "Payment Done" : "Payment Pending");
            referralDetails.put("referrals", getReferralTree(referral));
            referralTree.add(referralDetails);
        }
        return referralTree;
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
}
