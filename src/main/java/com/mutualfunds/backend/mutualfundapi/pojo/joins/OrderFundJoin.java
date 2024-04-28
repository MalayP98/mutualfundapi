package com.mutualfunds.backend.mutualfundapi.pojo.joins;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFundJoin {

    private Double investedValue;
    private String fundName;
    private String fundStrategy;
    private Double units;
    
}
