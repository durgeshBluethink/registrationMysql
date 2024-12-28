package net.uway.journalApp.controller;

import net.uway.journalApp.dto.PaymentDto;
import net.uway.journalApp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    public ResponseEntity<String> createPayment(@RequestBody PaymentDto paymentDto) {
        logger.info("Received payment request: " + paymentDto);
        try {
            String paymentResult = paymentService.createPayment(paymentDto);
            return ResponseEntity.ok(paymentResult);
        } catch (Exception e) {
            logger.severe("Error during payment processing: " + e.getMessage());
            return ResponseEntity.status(500).body("Payment failed: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
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
