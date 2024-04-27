package com.mutualfunds.backend.mutualfundapi.pojo.entity;

import javax.persistence.*;

@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "user_id")
    private Long userId;
}
