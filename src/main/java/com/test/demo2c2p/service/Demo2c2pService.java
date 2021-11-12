package com.test.demo2c2p.service;

import com.test.demo2c2p.api.request.GenerateJWTTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Slf4j
@RequiredArgsConstructor
@Service
public class Demo2c2pService {

    private final JwtService jwtService;
    private final HttpService httpService;

    public String generateJWTToken(GenerateJWTTokenRequest generateJWTTokenRequest) throws Exception {

        HashMap<String, Object> payload = makePayload(generateJWTTokenRequest);
        String token = jwtService.getToken(payload);
        String requestData = httpService.sendRequest(token);
        String paymentToken = jwtService.process(requestData);
        String result = httpService.doPayment(paymentToken);
        JSONParser parser = new JSONParser();
        JSONObject responseJSON = (JSONObject) parser.parse(result);
        String data = responseJSON.get("data").toString();
        return data;

    }

    private HashMap<String, Object> makePayload(GenerateJWTTokenRequest generateJWTTokenRequest) {
        HashMap<String, Object> payload = new HashMap<>();
        
        generateJWTTokenRequest.setInvoiceNo("1234123412341264");
        //generateJWTTokenRequest.setPaymentChannel(Arrays.asList("GRAB"));
        payload.put("merchantID", generateJWTTokenRequest.getMerchantID());
        payload.put("invoiceNo", generateJWTTokenRequest.getInvoiceNo());
        payload.put("description", generateJWTTokenRequest.getDescription());
        payload.put("amount", generateJWTTokenRequest.getAmount());
        payload.put("currencyCode", generateJWTTokenRequest.getCurrencyCode());
        //payload.put("paymentChannel", generateJWTTokenRequest.getPaymentChannel());

        log.debug("payload={},", payload);
        return payload;
    }

}
