package com.test.demo2c2p.api.request;
import lombok.Data;

@Data
public class PaymentActionRequest {
    private String invoiceNo;
    private String version;
    private String processType;
    private String merchantID;
    private String amount = "0.00";
}
