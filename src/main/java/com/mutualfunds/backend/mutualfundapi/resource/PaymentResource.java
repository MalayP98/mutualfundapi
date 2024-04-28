package com.mutualfunds.backend.mutualfundapi.resource;

import com.mutualfunds.backend.mutualfundapi.daos.PaymentDAO;
import com.mutualfunds.backend.mutualfundapi.dto.PaymentResponseDTO;
import com.mutualfunds.backend.mutualfundapi.services.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-gateway")
@RequiredArgsConstructor
public class PaymentResource {

    private final PaymentService paymentService;

    @PostMapping("/link")
    public ResponseEntity<PaymentResponseDTO> getPaymentLink(@RequestBody PaymentDAO paymentInfo) {
        return ResponseEntity.ok(paymentService.getPaymentLink(paymentInfo));
    }
}
