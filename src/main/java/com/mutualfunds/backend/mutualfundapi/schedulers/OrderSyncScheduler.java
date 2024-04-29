package com.mutualfunds.backend.mutualfundapi.schedulers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.dto.OrderDTO;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Order;
import com.mutualfunds.backend.mutualfundapi.services.OrderService;
import com.mutualfunds.backend.mutualfundapi.services.RTAService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class OrderSyncScheduler {

    private static final long expiryThresholdInMinutes = 5;

    private final OrderService orderService;

    private final RTAService rtaService;

    @Scheduled(initialDelay = 60, fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void syncOrders() {
        
        log.info("{} started.", getClass().getSimpleName());
        //Fetch all orders in SUBMITTED status
        List<Order> allSubmittedOrders = orderService.fetchOrdersByStatus(Order.TransactionStatus.SUBMITTED);

        //Fail all orders with time difference >  expiry threshold
        allSubmittedOrders.stream().filter(
                this::checkOrderExpiry
        ).forEach((order) -> {
            CompletableFuture.runAsync(() -> orderService.updateOrder(order, new OrderDTO(), Order.TransactionStatus.FAILED));
        });

        List<Order> pendingOrders = allSubmittedOrders.stream().filter(
            (order) -> !checkOrderExpiry(order)
        ).collect(Collectors.toList());

        //Update order status  accordingly (FAILED/REJECTED)
        for(Order order : pendingOrders){
            try {
                OrderDTO orderDTO = rtaService.fetchOrder(order.getOrderId());
                if(orderDTO.getSucceededAt() != null) {
                    CompletableFuture.runAsync(() -> orderService.updateOrder(order, orderDTO, Order.TransactionStatus.SUCCEEDED));
                } else if(orderDTO.getFailedAt() != null) {
                    CompletableFuture.runAsync(() -> orderService.updateOrder(order, orderDTO, Order.TransactionStatus.FAILED));
                }
            } catch (IOException e) {
                log.error("Failed to fetch order. Error message {}", e.getMessage());
            }
        }
    }

    private boolean checkOrderExpiry(Order order) {
        try {
            return (TimeUnit.MILLISECONDS.toMinutes(
                    System.currentTimeMillis()-order.getCreatedDate().getTime())
            ) > expiryThresholdInMinutes;
        } catch (Exception e) {
            log.error("Failed to calculate time difference. Error message {}", e.getMessage());
            return false;
        }
    }
}
