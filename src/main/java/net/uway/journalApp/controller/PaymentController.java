package net.uway.journalApp.controller;

import net.uway.journalApp.dto.PaymentDto;
import net.uway.journalApp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestBody PaymentDto paymentDto, @RequestParam UUID userId) { // Change Long to UUID
        logger.info("Received payment request: " + paymentDto + " for userId: " + userId);
        try {
            String paymentResult = paymentService.createPayment(paymentDto, userId);
            return ResponseEntity.ok(paymentResult);
        } catch (Exception e) {
            logger.severe("Error during payment processing: " + e.getMessage());
            return ResponseEntity.status(500).body("Payment failed: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePayment(@RequestParam String orderId, @RequestParam String paymentId, @RequestParam String status) {
        logger.info("Received payment update request: orderId=" + orderId + ", paymentId=" + paymentId + ", status=" + status);
        try {
            paymentService.updatePayment(orderId, paymentId, status);
            return ResponseEntity.ok("Payment updated successfully.");
        } catch (Exception e) {
            logger.severe("Error during payment update: " + e.getMessage());
            return ResponseEntity.status(500).body("Payment update failed: " + e.getMessage());
        }
    }
}
