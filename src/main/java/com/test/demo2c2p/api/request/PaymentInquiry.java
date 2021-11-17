package com.test.demo2c2p.api.request;

import lombok.Data;

@Data
public class PaymentInquiry {
    private String paymentToken;
    private String merchantID;
    private String invoiceNo;
}

