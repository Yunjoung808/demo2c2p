package com.test.demo2c2p.api.request;

import java.util.List;

import lombok.Data;

@Data
public class GenerateJWTTokenRequest {
    private String merchantID;
    private String invoiceNo;
    private String description;
    private double amount;
    private String currencyCode;
    private List<String> paymentChannel;
}
