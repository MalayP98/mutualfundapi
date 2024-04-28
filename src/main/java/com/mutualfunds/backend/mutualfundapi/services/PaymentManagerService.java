package com.mutualfunds.backend.mutualfundapi.services;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.mutualfunds.backend.mutualfundapi.constants.ApiConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentManagerService {

    public String createPaymentCall(String paymentInfo) throws IOException, HttpClientErrorException, HttpServerErrorException {
        String url = ApiConstants.PAYMENT_GATEWAY_URL + "/payment";
        RequestBody body = ApiConstants.getRequestBody(paymentInfo);
        Request request = new Request.Builder().url(url).method("POST", body).addHeader("Content-Type", "application/json").build();
        Response response = ApiConstants.API_CLIENT.newCall(request).execute();

        if(response.code() != 200) {
            if(response.code() == 400) {
                log.error("Bad Request Error");
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            } else {
                log.error("Failed to fetch from payment server");
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return response.body().string();
        
    }

    public String getPaymentCall(String paymentId) throws IOException, HttpClientErrorException, HttpServerErrorException {
        String url = ApiConstants.PAYMENT_GATEWAY_URL + "/payment/" + paymentId;
        RequestBody body = ApiConstants.getRequestBody("");
        Request request = new Request.Builder().url(url).method("GET", body).build();
        Response response = ApiConstants.API_CLIENT.newCall(request).execute();
        if(response.code() != 200) {
            if(response.code() == 400) {
                log.error("Bad Request Error");
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            } else {
                log.error("Failed to fetch from payment server");
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return response.body().string();
    }
}
