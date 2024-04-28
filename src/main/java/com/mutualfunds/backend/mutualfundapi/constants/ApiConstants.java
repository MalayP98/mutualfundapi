package com.mutualfunds.backend.mutualfundapi.constants;

import java.util.Map;

import org.springframework.http.HttpStatus;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiConstants {

    private ApiConstants(){

    }

    public static final OkHttpClient API_CLIENT = new OkHttpClient().newBuilder().build();
    public static final MediaType JSON_MEDIA = MediaType.parse("application/json");
    public static final String PAYMENT_GATEWAY_URL = "http://localhost:8080";
    public static final String RTA_GATEWAY_URL = "http://localhost:8081";
    
    public static RequestBody getRequestBody(String requestJson) {
        return RequestBody.create(requestJson, JSON_MEDIA);
    }
    
}
