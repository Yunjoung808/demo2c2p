package com.test.demo2c2p.service;

import com.test.demo2c2p.api.request.GenerateJWTTokenRequest;
import com.test.demo2c2p.api.request.CancelRequest;
import com.test.demo2c2p.api.response.RedirectResponse;
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

    public void parseDetails(RedirectResponse redirectResponse) throws Exception{
        String invoiceNo = redirectResponse.getInvoiceNo();
        String channelCode = redirectResponse.getChannelCode();
        String respCode = redirectResponse.getRespCode();
        String respDesc = redirectResponse.getRespDesc();
        log.debug(String.format("invoice: %s, code: %s, resp code: %s, respDesc: %s",invoiceNo,channelCode,respCode,respDesc));


    }

    public String sendCancelRequest(CancelRequest cancelRequest) throws Exception{
        String paymentToken = cancelRequest.getPaymentToken();
        String clientID = cancelRequest.getClientID();
        String result = httpService.doCancellation(paymentToken,clientID);
        log.debug("\nresult = {}\n\n",result);
        JSONParser parser = new JSONParser();
        JSONObject responseJSON = (JSONObject) parser.parse(result);
        String data = responseJSON.get("data").toString();
        return data;

    }

    public String generateJWTToken(GenerateJWTTokenRequest generateJWTTokenRequest) throws Exception {

        HashMap<String, Object> payload = makePayload(generateJWTTokenRequest);
        String token = jwtService.getToken(payload);
        String requestData = httpService.sendRequest(token);
        String paymentToken = jwtService.process(requestData);
        String channelCode = generateJWTTokenRequest.getPaymentChannel().get(0);
        String result = httpService.doPayment(paymentToken,channelCode);
        JSONParser parser = new JSONParser();
        JSONObject responseJSON = (JSONObject) parser.parse(result);
        log.debug("\nresult = {}\n\n",result);
        String data = responseJSON.get("data").toString();

        return data;

    }

    private HashMap<String, Object> makePayload(GenerateJWTTokenRequest generateJWTTokenRequest) {
        HashMap<String, Object> payload = new HashMap<>();
        

        String frontendReturnURL = "localhost:8080/frontendredirect";
        generateJWTTokenRequest.setAmount(0.01);
        payload.put("frontendReturnURL",frontendReturnURL);
        payload.put("merchantID", generateJWTTokenRequest.getMerchantID());
        payload.put("invoiceNo", generateJWTTokenRequest.getInvoiceNo());
        payload.put("description", generateJWTTokenRequest.getDescription());
        payload.put("amount", generateJWTTokenRequest.getAmount());
        payload.put("currencyCode", generateJWTTokenRequest.getCurrencyCode());
        payload.put("paymentChannel", generateJWTTokenRequest.getPaymentChannel());

        log.debug("payload to server={},", payload);
        return payload;
    }

}
