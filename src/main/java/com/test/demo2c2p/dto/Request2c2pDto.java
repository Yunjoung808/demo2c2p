package com.test.demo2c2p.dto;

import java.util.List;

import lombok.Data;

@Data
public class Request2c2pDto {
    private String merchantID;
    private String invoiceNo;
    private String description;
    private double amount;
    private String currencyCode;
    private List<String> paymentChannel;

}
