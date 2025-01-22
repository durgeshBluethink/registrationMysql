package net.uway.journalApp.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import net.uway.journalApp.dto.PaymentDto;
import net.uway.journalApp.entity.Payment;
import net.uway.journalApp.entity.PaymentStatus;  // Import PaymentStatus enum
import net.uway.journalApp.entity.User;
import net.uway.journalApp.repository.PaymentRepository;
import net.uway.journalApp.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    // Method to create a payment
    public String createPayment(PaymentDto paymentDto, long userId) {
        paymentDto.validateAmount(); // Ensure the amount is valid

        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();

            orderRequest.put("amount", (Object)(paymentDto.getAmount() * 100)); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis()); // Use this as receiptId

            Order order = client.orders.create(orderRequest);

            Payment payment = new Payment();
            payment.setOrderId(order.get("id")); // Store the Razorpay order ID
            payment.setAmount(paymentDto.getAmount());
            payment.setStatus(PaymentStatus.CREATED);  // Set status as CREATED initially

            // Store both the Razorpay paymentId and receiptId
            payment.setPaymentId(order.get("id")); // Store the payment ID
            payment.setReceiptId(order.get("receipt")); // Store the receipt ID
            payment.setCreatedAt(LocalDateTime.now());

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            payment.setUser(user);

            paymentRepository.save(payment);

            return order.toString(); // Return order details for further processing
        } catch (RazorpayException e) {
            logger.severe("Error during payment processing: " + e.getMessage());
            throw new RuntimeException("Payment creation failed", e);
        }
    }


    // Method to update the payment status
    public void updatePayment(String orderId, String paymentId, String status) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("Payment not found"));
        logger.info("Updating payment with Order ID: " + orderId + ", Payment ID: " + paymentId + ", Status: " + status);

        // Set both paymentId and status (converted to PaymentStatus enum)
        payment.setPaymentId(paymentId);
        payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));  // Convert the string status to enum

        paymentRepository.save(payment);
    }


}
