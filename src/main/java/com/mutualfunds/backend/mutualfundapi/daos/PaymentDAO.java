package com.mutualfunds.backend.mutualfundapi.daos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDAO {

    private Integer amount;

    private Long productId;

    private String accountNumber;

    private String ifscCode;
}
