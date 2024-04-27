package com.mutualfunds.backend.mutualfundapi.pojo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder
public class Payment extends AuditingEntity {

    public enum TransactionType{
        CREDIT,
        DEBIT;
    }

    public enum TransactionStatus{
        PENDING,
        COMPLETE,
        FAILED;
    }

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "txnId")
    private String transactionId;
}
