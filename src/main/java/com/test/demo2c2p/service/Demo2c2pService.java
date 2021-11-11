package com.test.demo2c2p.service;

import com.test.demo2c2p.api.request.GenerateJWTTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class Demo2c2pService {

    private final JwtService jwtService;
    private final HttpService httpService;

    public void generateJWTToken(GenerateJWTTokenRequest generateJWTTokenRequest) throws Exception {

        HashMap<String, Object> payload = makePayload(generateJWTTokenRequest);
        String token = jwtService.getToken(payload);

        JSONObject requestData = new JSONObject();
        requestData.put("payload", token);
        log.debug("requestData={}", requestData);

        HttpsURLConnection con = httpService.getConnection();
        String response = httpService.getResponse(requestData, con);
        log.debug("response={}", response);

        jwtService.process();
        log.info("==========END :: generateJWTToken ===============");

    }

    private HashMap<String, Object> makePayload(GenerateJWTTokenRequest generateJWTTokenRequest) {
        HashMap<String, Object> payload = new HashMap<>();

        payload.put("merchantID", generateJWTTokenRequest.getMerchantID());
        payload.put("invoiceNo", generateJWTTokenRequest.getInvoiceNo());
        payload.put("description", generateJWTTokenRequest.getDescription());
        payload.put("amount", generateJWTTokenRequest.getAmount());
        payload.put("currencyCode", generateJWTTokenRequest.getCurrencyCode());
        payload.put("paymentChannel", generateJWTTokenRequest.getPaymentChannel());

        log.debug("payload={},", payload);
        return payload;
    }

}
