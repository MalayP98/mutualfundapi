package com.mutualfunds.backend.mutualfundapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.daos.PaymentDAO;
import com.mutualfunds.backend.mutualfundapi.dto.PaymentResponseDTO;
import com.mutualfunds.backend.mutualfundapi.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-gateway")
@RequiredArgsConstructor
@Slf4j
public class PaymentResource {

    private final PaymentService paymentService;

    @PostMapping("/link")
    public ResponseEntity<PaymentResponseDTO> getPaymentLink(@RequestBody Map<String, Object> reqBody){
        PaymentDAO paymentInfo = PaymentDAO.builder()
                                    .accountNumber((String) reqBody.get("accountNumber"))
                                    .amount((Double) reqBody.get("amount"))
                                    .ifscCode((String) reqBody.get("ifscCode"))
                                    .redirectUrl((String) reqBody.get("redirectUrl"))
                                    .build();
        Long strategyId = (Long) reqBody.get("strategyId");
        return ResponseEntity.ok(paymentService.getPaymentLink(paymentInfo, strategyId));
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<Boolean> confirmPayment() {
        try {
            boolean result = paymentService.checkPayment();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Failed to confirm payment" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}
