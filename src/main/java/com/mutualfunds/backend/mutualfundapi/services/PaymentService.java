package com.mutualfunds.backend.mutualfundapi.services;

import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.daos.PaymentDAO;
import com.mutualfunds.backend.mutualfundapi.dto.PaymentResponseDTO;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Payment;
import com.mutualfunds.backend.mutualfundapi.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserService userService;

    private final PaymentManagerService paymentManagerService;

    public PaymentResponseDTO getPaymentLink(PaymentDAO paymentInfo) {
        try {
            //Make payload json from DAO
            String paymentDAOJson = JsonConstants.OBJECT_MAPPER.writeValueAsString(paymentInfo);
            //Rest Api call;
            String results = paymentManagerService.createPaymentCall(paymentDAOJson);
            //get  the response DTO from the result string
            PaymentResponseDTO paymentResponse = JsonConstants.OBJECT_MAPPER.readValue(results, PaymentResponseDTO.class);
            CompletableFuture.runAsync(() -> savePayment(paymentResponse, paymentInfo));
            return paymentResponse;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error in getting payment link");
            return PaymentResponseDTO.genericFailureResponse("ERROR: " + e.getMessage());
        }
    }

    

    private Payment savePayment(PaymentResponseDTO paymentResponse, PaymentDAO paymentInfo){
        String link = paymentResponse.getPaymentLink();
        String[] tokens = link.split("//");
        return paymentRepository.save(Payment
                .builder()
                .userId(userService.currentUser().getId())
                .status(Payment.TransactionStatus.PENDING)
                .amount(paymentInfo.getAmount())
                .productId(paymentInfo.getProductId())
                .transactionType(Payment.TransactionType.CREDIT)
                .transactionId(tokens[tokens.length-1])
                .build());
    }
}
