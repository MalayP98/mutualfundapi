package com.mutualfunds.backend.mutualfundapi.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mutualfunds.backend.mutualfundapi.pojo.entity.Fund;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.FundStrategy;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.Order;
import com.mutualfunds.backend.mutualfundapi.pojo.joins.OrderFundJoin;
import com.mutualfunds.backend.mutualfundapi.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

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
}
