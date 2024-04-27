package com.mutualfunds.backend.mutualfundapi.pojo.entity;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order extends AuditingEntity{

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "user_id")
    private Long userId;
}
