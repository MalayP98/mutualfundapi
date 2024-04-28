package com.mutualfunds.backend.mutualfundapi.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

@RestController("/payments")
public class PaymentController {

//    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
//
//    @PostMapping("/payment-create")
//    public String createPaymentLink(@RequestHeader("user-phone") String userPhone,
//            @RequestBody Map<String, Object> paymentRequest) throws Exception{
//        // Get User
//
//        // Logic to implement payment info
//        /*
//         * {
//         * "accountNumber": "11200222",
//         * "ifscCode": "UBIT22222",
//         * "amount": 500,
//         * "redirectUrl": "http://localhost:3000"
//         * }
//         */
//
//        // Check User
//
//        // Create payment api payload
//        String[] keys = { "accountNumber", "ifscCode", "amount", "redirectUrl", "strategy"};
//        Map<String, Object> requestBody = new HashMap<>();
//
//        for (String key : keys) {
//            if (!paymentRequest.containsKey(key))
//                throw new Exception("Missing parameter " + key);
//            else {
//                Object value = paymentRequest.get(key);
//                if (value == null)
//                    throw new Exception("Null  Value not allowed for parameter " + key);
//                requestBody.put(key, value);
//            }
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonBody = mapper.writeValueAsString(requestBody);
//        String paymentGatewayUrl = "http://localhost:8080" + "/payment";
//
//        // Call payment gateway service
//        MediaType mediaType = MediaType.parse("application/json");
//        okhttp3.RequestBody body = okhttp3.RequestBody.create(jsonBody, mediaType);
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(paymentGatewayUrl)
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .build();
//        okhttp3.Response response = client.newCall(request).execute();
//        int responseCode = response.code();
//        if (responseCode != 200) {
//            throw new Exception("Failed to create payment");
//        }
//        String jsonString = response.body().string();
//        Map<String, Object> responseBodyMap = mapper.readValue(jsonString, Map.class);
//
//        // Extract payment id from response
//        String paymentPageUrl = (String) responseBodyMap.get("paymentLink");
//        String[] pathParts = paymentPageUrl.split("/");
//        String paymentId = pathParts[pathParts.length - 1];
//
//        // Store in User Db
//
//        return paymentPageUrl;
//    }

}
