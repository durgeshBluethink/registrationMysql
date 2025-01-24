package org.uway.NarayanSena.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.uway.NarayanSena.dto.PaymentDto;
import org.uway.NarayanSena.entity.Payment;
import org.uway.NarayanSena.entity.PaymentStatus;
import org.uway.NarayanSena.entity.User;
import org.uway.NarayanSena.exception.PaymentCreationFailedException;
import org.uway.NarayanSena.exception.PaymentNotFoundException;
import org.uway.NarayanSena.exception.UserNotFoundException;
import org.uway.NarayanSena.repository.PaymentRepository;
import org.uway.NarayanSena.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Autowired
    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    public String createPayment(PaymentDto paymentDto, long userId) {
        paymentDto.validateAmount();

        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();

            orderRequest.put("amount", (Object) (paymentDto.getAmount() * 100));
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create(orderRequest);

            Payment payment = Payment.builder()
                    .orderId(order.get("id"))
                    .amount(paymentDto.getAmount())
                    .status(PaymentStatus.CREATED)
                    .paymentId(order.get("id"))
                    .receiptId(order.get("receipt"))
                    .createdAt(LocalDateTime.now())
                    .user(userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId))) // Corrected lambda expression
                    .build();

            paymentRepository.save(payment);

            return order.toString();
        } catch (RazorpayException e) {
            logger.severe("Error during payment processing: " + e.getMessage());
            throw new PaymentCreationFailedException("Payment creation failed: " + e.getMessage(), e);
        }
    }
    public void updatePaymentStatus(String orderId, String status) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with orderId: " + orderId + " not found."));

        switch (status) {
            case "success":
                payment.setStatus(PaymentStatus.SUCCESS);
                break;
            case "failed":
                payment.setStatus(PaymentStatus.FAILED);
                break;
            case "canceled":
                payment.setStatus(PaymentStatus.CANCELED);
                break;
            default:
                // Handle unknown status
                throw new IllegalArgumentException("Invalid payment status: " + status);
        }

        paymentRepository.save(payment);
    }

    public void updatePayment(String orderId, String paymentId, String status) {
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);

        if (optionalPayment.isEmpty()) {
            throw new PaymentNotFoundException("Payment with orderId: " + orderId + " not found.");
        }

        Payment payment = optionalPayment.get();

        logger.info("Updating payment with Order ID: " + orderId + ", Payment ID: " + paymentId + ", Status: " + status);

        payment.setPaymentId(paymentId);
        payment.setStatus(PaymentStatus.fromString(status));

        paymentRepository.save(payment);
    }
}