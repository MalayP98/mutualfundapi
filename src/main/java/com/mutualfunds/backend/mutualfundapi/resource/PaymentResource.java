package com.mutualfunds.backend.mutualfundapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
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
    public ResponseEntity<PaymentResponseDTO> getPaymentLink(@RequestBody PaymentDAO paymentInfo){
        return ResponseEntity.ok(paymentService.getPaymentLink(paymentInfo));
    }

//    public static void main(String[] args) throws JsonProcessingException {
//        PaymentDAO paymentDAO = new PaymentDAO();
//        System.out.println(JsonConstants.OBJECT_MAPPER.writeValueAsString(paymentDAO));
//    }
}
