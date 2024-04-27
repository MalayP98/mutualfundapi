package com.mutualfunds.backend.mutualfundapi.repositories;

import com.mutualfunds.backend.mutualfundapi.pojo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
