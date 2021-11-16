package com.test.demo2c2p.api.response;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RedirectResponse {
    private String invoiceNo;
    private String channelCode;
    private String respCode;
    private String respDesc;

}
