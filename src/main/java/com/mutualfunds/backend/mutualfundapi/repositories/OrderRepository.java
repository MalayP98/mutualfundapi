package com.mutualfunds.backend.mutualfundapi.repositories;

import com.mutualfunds.backend.mutualfundapi.pojo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
