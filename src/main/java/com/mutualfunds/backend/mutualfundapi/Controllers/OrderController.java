package com.mutualfunds.backend.mutualfundapi.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/orders")
public class OrderController {

    @PostMapping("/createOrder")
    public boolean createOrder(@RequestHeader("user-phone") String userPhone,
            @RequestBody Map<String, Object> createOrderRequest) throws Exception{
        /*
         * 
         * {
         * "fund": "Arbitrage Fund 1",
         * "amount": 500,
         * "paymentID": "3b84fe7b-f044-4277-91bf-712194201b34"
         * }
         */
        String[] paymentRequestKeys = {"fund", "amount", "paymentID"};
        Map<String, Object> createOrderPayload = new HashMap<>();
        for(String key : paymentRequestKeys){
            if(!createOrderRequest.containsKey(key)){
                throw new IllegalArgumentException("Missing parameter: " + key);
            }
            Object value = createOrderRequest.get(key);
            if(value == null) {
                throw new IllegalArgumentException("Null parameter not allowed in key: " + key);
            }
            createOrderPayload.put(key, value);
        }
        return true;
    }

    @GetMapping("/getPortfolio")
    public Map<String, Object> getPortfolio(@RequestHeader("user-phone") String userPhone) {
        return null;
    }
}
