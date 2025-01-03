package net.uway.journalApp.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import net.uway.journalApp.dto.PaymentDto;
import net.uway.journalApp.entity.Payment;
import net.uway.journalApp.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public String createPayment(PaymentDto paymentDto) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", paymentDto.getAmount() * 100); // amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create(orderRequest);

            Payment payment = new Payment();
            payment.setOrderId(order.get("id"));
            payment.setAmount(paymentDto.getAmount());
            payment.setStatus("created");
            payment.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            return order.toString();
        } catch (RazorpayException e) {
            logger.severe("Error during payment processing: " + e.getMessage());
            throw new RuntimeException("Payment creation failed", e);
        }
    }

    public void updatePayment(String orderId, String paymentId, String status) {
        Payment payment = paymentRepository.findById(Long.valueOf(orderId)).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentId(paymentId);
        payment.setStatus(status);

        paymentRepository.save(payment);
    }
}
