package com.mutualfunds.backend.mutualfundapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mutualfunds.backend.mutualfundapi.constants.ApiConstants;
import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.daos.PaymentDAO;
import com.mutualfunds.backend.mutualfundapi.dto.CheckPaymentDTO;
import com.mutualfunds.backend.mutualfundapi.dto.PaymentResponseDTO;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Fund;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Payment;
import com.mutualfunds.backend.mutualfundapi.repositories.FundStrategyRepository;
import com.mutualfunds.backend.mutualfundapi.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserService userService;

    private final PaymentManagerService paymentManagerService;

    private final FundStrategyRepository fundStrategyRepository;

    private final OrderService orderService;

    public PaymentResponseDTO getPaymentLink(PaymentDAO paymentInfo) {
        try {
            // Make payload json from DAO
            String paymentDAOJson = JsonConstants.OBJECT_MAPPER.writeValueAsString(paymentInfo);
            // Rest Api call;
            String results = paymentManagerService.createPaymentCall(paymentDAOJson);
            // get the response DTO from the result string
            PaymentResponseDTO paymentResponse = JsonConstants.OBJECT_MAPPER.readValue(results,
                    PaymentResponseDTO.class);
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
                .transactionId(tokens[tokens.length - 1])
                .build());
    }

    public boolean checkPayment() throws HttpClientErrorException, HttpServerErrorException, IOException {
        // Fetch user id
        Long userId = userService.currentUser().getId();
        // Get all pending payments from user
        List<Payment> pendingPayments = paymentRepository.findByUserIdAndStatusAndTransactionType(userId,
                Payment.TransactionStatus.PENDING, Payment.TransactionType.CREDIT);
        // Check Payment status
        List<Payment> completedPayments = new ArrayList<>();
        for (Payment item : pendingPayments) {
            int maxRetries = ApiConstants.MAX_RETRIES;
            boolean success = checkPaymentStatus(item, maxRetries);
            if (success) {
                completedPayments.add(item);
            }
            CompletableFuture.runAsync(() -> updateTransactionStatus(item, success));

        }

        if (completedPayments.size() == 0) {
            return false;
        }

        // From list of successful Payment Entries place orders
        for (Payment item : completedPayments) {
            // Fetch funds associated with strategy
            Long strategyId = item.getProductId();
            List<Fund> fundList = fundStrategyRepository.findFundsByStrategyId(strategyId);
            // For each fund place order
            orderService.placeOrders(userId, item, fundList);

        }

        return true;

    }

    private boolean checkPaymentStatus(Payment item, int retries)
            throws IOException, JsonProcessingException, JsonMappingException {
        boolean success = false;
        while (retries > 0) {
            try {
                String response = paymentManagerService.getPaymentCall(item.getTransactionId());
                CheckPaymentDTO checkPayment = JsonConstants.OBJECT_MAPPER.readValue(response, CheckPaymentDTO.class);
                if (checkPayment.getStatus() == "Success") {
                    success = true;
                    break;
                }
            } catch (HttpClientErrorException e) {
                // TODO Auto-generated catch block
                log.error("Failed payment attempt" + e.getMessage());
            } catch (HttpServerErrorException e) {
                // TODO Auto-generated catch block
                log.error("Failed payment attempt" + e.getMessage());
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                log.error("Failed payment attempt" + e.getMessage());
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                log.error("Failed payment attempt" + e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("Failed payment attempt" + e.getMessage());
            } finally {
                retries--;
            }
        }
        return success;
    }

    private void updateTransactionStatus(Payment item, boolean success) {
        if (!success) {
            item.setStatus(Payment.TransactionStatus.FAILED);
        } else {
            item.setStatus(Payment.TransactionStatus.COMPLETE);
        }
        paymentRepository.save(item);
    }
}
