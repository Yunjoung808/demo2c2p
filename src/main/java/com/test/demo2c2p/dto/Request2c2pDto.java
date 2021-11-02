package com.test.demo2c2p.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class Request2c2pDto {
    private String merchantID;
    private String invoiceNo;
    private String description;
    private BigDecimal amount;
    private String currencyCode;
    private List<String> paymentChannel;

}
