package com.mutualfunds.backend.mutualfundapi.daos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentDAO {

    private Double amount;

    private String accountNumber;

    private String ifscCode;

    private String redirectUrl;
}
