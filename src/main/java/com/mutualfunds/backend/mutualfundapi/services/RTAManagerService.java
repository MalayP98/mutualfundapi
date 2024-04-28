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
import okhttp3.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class RTAManagerService {

    public String createOrder(String jsonBody) throws IOException, HttpClientErrorException, HttpServerErrorException{
        String url = ApiConstants.RTA_GATEWAY_URL + "/order";
        Request request = new Request.Builder()
        .url(url)
        .method("POST", ApiConstants.getRequestBody(jsonBody))
        .addHeader("Content-Type", "application/json")
        .build();
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

    public String fetchOrder(String orderId) throws IOException, HttpClientErrorException, HttpServerErrorException{
        String url = ApiConstants.RTA_GATEWAY_URL + "/order/" + orderId;
        Request request = new Request.Builder()
        .url(url)
        .method("GET", ApiConstants.getRequestBody(""))
        .addHeader("Content-Type", "application/json")
        .build();
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

    public String fetchFundMarketValue(String fundName) throws IOException, HttpClientErrorException, HttpServerErrorException{
        String url = ApiConstants.RTA_GATEWAY_URL + "/market-value/" + fundName;
        Request request = new Request.Builder()
        .url(url)
        .method("GET", ApiConstants.getRequestBody(""))
        .addHeader("Content-Type", "application/json")
        .build();
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
