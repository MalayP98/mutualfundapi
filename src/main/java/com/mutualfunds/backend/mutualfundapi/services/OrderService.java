package com.mutualfunds.backend.mutualfundapi.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mutualfunds.backend.mutualfundapi.OrderFundJoinMapper;
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
            return OrderFundJoinMapper.objectListToOrderFundJoinList(orderRepository.findOrdersJoinFundsByUserId(userId));
        } catch (Exception e) {
            log.error("Failed to fetch  orders by user id", e);
            throw new SQLException("Failed to fetch orders");
        }
    }
}
