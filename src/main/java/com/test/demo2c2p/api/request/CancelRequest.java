package com.test.demo2c2p.api.request;

import lombok.Data;

@Data
public class CancelRequest {
    private String paymentToken;
    private String clientID;
}

