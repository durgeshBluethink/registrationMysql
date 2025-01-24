package org.uway.NarayanSena.services;

import jakarta.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uway.NarayanSena.dto.ReferralDTO;
import org.uway.NarayanSena.dto.UserDetailsDto;
import org.uway.NarayanSena.dto.UserDto;
import org.uway.NarayanSena.entity.Payment;
import org.uway.NarayanSena.entity.PaymentStatus;
import org.uway.NarayanSena.entity.User;
import org.uway.NarayanSena.exception.UserNotFoundException;
import org.uway.NarayanSena.repository.PaymentRepository;
import org.uway.NarayanSena.repository.UserRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private static final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentRepository paymentRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public User registerUser(UserDto userDto) {
        logger.info("Registering user with email: " + userDto.getEmail());

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists. Please use a different email.");
        }

        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setCity(userDto.getCity());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        String referralId = generateReferralId();
        user.setReferralId(referralId);

        if (userDto.getReferrerId() != null && !userDto.getReferrerId().isEmpty()) {
            User referrer = userRepository.findByReferralId(userDto.getReferrerId())
                    .orElseThrow(() -> new RuntimeException("Invalid referrer ID"));
            user.setReferrer(referrer);
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
        logger.info("Fetching user details for userId: " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Hibernate.initialize(user.getReferredUsers());
        Hibernate.initialize(user.getPayments());

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId(user.getId());
        userDetailsDto.setFullName(user.getFullName());
        userDetailsDto.setEmail(user.getEmail());
        userDetailsDto.setMobileNumber(user.getMobileNumber());
        userDetailsDto.setCity(user.getCity());
        userDetailsDto.setReferrer(user.getReferrer() != null ? user.getReferrer().getFullName() : "No referrer");
        userDetailsDto.setReferrerId(user.getReferrer() != null ? user.getReferrer().getReferralId() : "No referrer ID");
        userDetailsDto.setReferralId(user.getReferralId());
        userDetailsDto.setReferralTree(getReferralTree(user));

        List<Payment> payments = paymentRepository.findPaymentsByUserId(userId);
        boolean isPaymentComplete = payments.stream()
                .anyMatch(payment -> PaymentStatus.CREATED == payment.getStatus());

        userDetailsDto.setPaymentComplete(isPaymentComplete);
        userDetailsDto.setPaymentStatus(isPaymentComplete ? "Payment Done" : "Payment Pending. Please complete your payment.");

        logger.info("Fetched user details: " + userDetailsDto);
        return userDetailsDto;
    }

    private List<ReferralDTO> getReferralTree(User user) {
        return user.getReferredUsers().stream()
                .map(referral -> {
                    ReferralDTO referralDTO = new ReferralDTO();
                    referralDTO.setFullName(referral.getFullName());
                    referralDTO.setEmail(referral.getEmail());
                    referralDTO.setPaymentStatus(referral.getPayments().stream()
                            .anyMatch(payment -> PaymentStatus.CREATED.equals(payment.getStatus())) ? "Payment Done" : "Payment Pending");                    referralDTO.setReferrals(getReferralTree(referral));
                    return referralDTO;
                })
                .collect(Collectors.toList());
    }
}
