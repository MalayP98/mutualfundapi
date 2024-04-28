package com.mutualfunds.backend.mutualfundapi.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.daos.CreateOrderDAO;
import com.mutualfunds.backend.mutualfundapi.dto.CreateOrderDTO;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Fund;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.FundStrategy;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Order;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Payment;
import com.mutualfunds.backend.mutualfundapi.pojo.joins.OrderFundJoin;
import com.mutualfunds.backend.mutualfundapi.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    private final RTAManagerService rtaManagerService;

    public List<OrderFundJoin> getAllOrdersByUserId(Long userId) throws SQLException{
        try {
            List<Object []> allOrdersResultSet = orderRepository.findOrdersJoinFundsByUserId(userId);
            List<OrderFundJoin> allOrders = new ArrayList<>();
            for(Object[] obj: allOrdersResultSet) {
                OrderFundJoin temp = new OrderFundJoin();
                Order tempOrder = (Order) obj[0];
                Fund tempFund = (Fund) obj[1];
                FundStrategy tempFundStrategy = (FundStrategy) obj[2];
                temp.setFundName(tempFund.getName());
                temp.setFundStrategy(tempFundStrategy.getName());
                temp.setInvestedValue(tempOrder.getAmount());
                temp.setUnits(tempOrder.getUnits());
                allOrders.add(temp);
            }
            return allOrders;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Failed to fetch  orders by user id", e);
            throw new SQLException("Failed to fetch orders");
        }
    }

    public void placeOrders(Long userId, Payment item, List<Fund> fundList)
            throws JsonProcessingException, IOException, JsonMappingException {
        for(Fund fund: fundList) {
            try {
                Integer percentage = fund.getPercentage();
                Double totalAmountInvested = item.getAmount();
                CreateOrderDAO createOrder = CreateOrderDAO.builder().amount(totalAmountInvested * percentage).paymentID(item.getTransactionId()).fund(fund.getName()).build();
                String jsonBody = JsonConstants.OBJECT_MAPPER.writeValueAsString(createOrder);
                String response = rtaManagerService.createOrder(jsonBody);
                CreateOrderDTO createdOrder= JsonConstants.OBJECT_MAPPER.readValue(response, CreateOrderDTO.class);
                saveOrder(userId, fund, createdOrder);
            } catch (HttpClientErrorException e) {
                // TODO Auto-generated catch block
                log.error("Failed to place Order: " + e.getMessage());
            } catch (HttpServerErrorException e) {
                // TODO Auto-generated catch block
                log.error("Failed to place Order: " + e.getMessage());
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                log.error("Failed to place Order: " + e.getMessage());
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                log.error("Failed to place Order: " + e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("Failed to place Order: " + e.getMessage());
            }
        }
    }

    private void saveOrder(Long userId, Fund fund, CreateOrderDTO createdOrder) {
        Order order = Order.builder().amount(createdOrder.getData().getAmount())
        .orderId(createdOrder.getData().getId())
        .productId(fund.getId())
        .units(createdOrder.getData().getUnits())
        .userId(userId)
        .status(Order.TransactionStatus.SUBMITTED)
        .build();
        
        orderRepository.save(order);
    }
}
