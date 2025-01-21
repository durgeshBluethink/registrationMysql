package net.uway.journalApp.services;

import jakarta.transaction.Transactional;
import net.uway.journalApp.dto.UserDto;
import net.uway.journalApp.dto.UserDetailsDto;
import net.uway.journalApp.dto.ReferralDTO;
import net.uway.journalApp.entity.User;
import net.uway.journalApp.exception.UserNotFoundException;
import net.uway.journalApp.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
        logger.info("Registering user with email: " + userDto.getEmail());

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
            referrer.getReferredUsers().add(user); // Add the new user to the referrer's list
        }

        return userRepository.save(user);
    }

    private String generateReferralId() {
        String referralId;
        do {
            int length = random.nextInt(3) + 3; // Generate a number between 3 and 5
            StringBuilder referralIdBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                referralIdBuilder.append(random.nextInt(10)); // Append a random digit
            }
            referralId = referralIdBuilder.toString();
        } while (userRepository.existsByReferralId(referralId)); // Ensure the generated ID is unique
        return referralId;
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
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            logger.warning("No user found with email: " + email);
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Transactional
    public UserDetailsDto getUserDetails(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Fetch related data separately
        Hibernate.initialize(user.getReferredUsers());
        Hibernate.initialize(user.getPayments());

        // Create DTO and populate fields
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId(user.getId());
        userDetailsDto.setFullName(user.getFullName());
        userDetailsDto.setEmail(user.getEmail());
        userDetailsDto.setMobileNumber(user.getMobileNumber());
        userDetailsDto.setCity(user.getCity());
        userDetailsDto.setReferrer(user.getReferrer() != null ? user.getReferrer().getFullName() : "No referrer");
        userDetailsDto.setReferrerId(user.getReferrer() != null ? user.getReferrer().getReferralId() : "No referrer ID");
        userDetailsDto.setReferralId(user.getReferralId());
        userDetailsDto.setPaymentComplete(user.isPaymentComplete());
        userDetailsDto.setReferralTree(getReferralTree(user));
        return userDetailsDto;
    }


    private List<ReferralDTO> getReferralTree(User user) {
        List<ReferralDTO> referralTree = new ArrayList<>();
        for (User referral : user.getReferredUsers()) {
            ReferralDTO referralDTO = new ReferralDTO();
            referralDTO.setFullName(referral.getFullName());
            referralDTO.setEmail(referral.getEmail());
            referralDTO.setPaymentStatus(referral.isPaymentComplete() ? "Payment Done" : "Payment Pending");
            referralDTO.setReferrals(getReferralTree(referral)); // recursively fetch referrals
            referralTree.add(referralDTO);
        }
        return referralTree;
    }
}
