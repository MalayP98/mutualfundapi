package com.mutualfunds.backend.mutualfundapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.daos.PaymentDAO;
import com.mutualfunds.backend.mutualfundapi.dto.PaymentResponseDTO;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Payment;
import com.mutualfunds.backend.mutualfundapi.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserService userService;

    public PaymentResponseDTO getPaymentLink(PaymentDAO paymentInfo){
        try {
            String payload = JsonConstants.OBJECT_MAPPER.writeValueAsString(paymentInfo);
            // TODO : send this payload to '/payment' endpoint and store result in 'result'.
            String result = "";
            PaymentResponseDTO paymentResponse = JsonConstants
                    .OBJECT_MAPPER
                    .readValue(result, PaymentResponseDTO.class);
            CompletableFuture.runAsync(() -> savePayment(paymentResponse, paymentInfo));
            return paymentResponse;
        } catch (JsonProcessingException e) {
            log.error("Unable to create payment link. Please try again");
        }
        return PaymentResponseDTO.genericFailureResponse();
    }

    private Payment savePayment(PaymentResponseDTO paymentResponse, PaymentDAO paymentInfo){
        String link = paymentResponse.getPaymentLink();
        String[] tokens = link.split("//");
        return paymentRepository.save(Payment
                .builder()
                .userId(userService.currentUser().getId())
                .status(Payment.TransactionStatus.PENDING)
                .productId(paymentInfo.getProductId())
                .transactionType(Payment.TransactionType.CREDIT)
                .transactionId(tokens[tokens.length-1])
                .build());
    }
}
