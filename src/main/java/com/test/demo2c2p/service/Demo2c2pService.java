package com.test.demo2c2p.service;

import com.test.demo2c2p.api.request.GenerateJWTTokenRequest;
import com.test.demo2c2p.api.request.PaymentInquiry;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.test.demo2c2p.api.request.CancelRequest;
import com.test.demo2c2p.api.response.RedirectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    public String doPaymentInquiry(PaymentInquiry paymentRequest) throws Exception{
        System.out.println("doPaymentInquiry");
        //1.payload만들기
        HashMap<String, Object> payload = makePayloadForPaymentInquiry(paymentRequest);

        //2.JWT토큰 만들기
        String JWToken = jwtService.getToken(payload);

        //3. paymentInquiry 로 전송
        JSONObject requestData = new JSONObject();
        requestData.put("payload",JWToken);
        String endPoint = "https://sandbox-pgw.2c2p.com/payment/4.1/PaymentInquiry";
        String requestPayload = httpService.getConnection(requestData, endPoint);
        System.out.println("requestPayload::"+requestPayload);
       
        //4.decode requestPayload
        // Map<String, Claim> result = jwtService.getDecodedPayload(requestPayload);

        // System.out.println(result);
        

      
        return "requestPayload";
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
        String url = jwtService.process(requestData);
        return url;
        // String paymentToken = jwtService.process(requestData);
        // String channelCode = generateJWTTokenRequest.getPaymentChannel().get(0);
        // String result = httpService.doPayment(paymentToken,channelCode);
        // JSONParser parser = new JSONParser();
        // JSONObject responseJSON = (JSONObject) parser.parse(result);
        // log.debug("\nresult = {}\n\n",result);
        // String data = responseJSON.get("data").toString();

        // return data;

    }

    public HashMap<String, Object> makePayload(GenerateJWTTokenRequest generateJWTTokenRequest) {
        HashMap<String, Object> payload = new HashMap<>();
        

        String frontendReturnURL = "localhost:8080/frontendredirect";
        generateJWTTokenRequest.setAmount(50.00);
        payload.put("frontendReturnURL",frontendReturnURL);
        payload.put("merchantID", generateJWTTokenRequest.getMerchantID());
        payload.put("invoiceNo", generateJWTTokenRequest.getInvoiceNo());
        payload.put("description", generateJWTTokenRequest.getDescription());
        payload.put("amount", generateJWTTokenRequest.getAmount());
        payload.put("currencyCode", generateJWTTokenRequest.getCurrencyCode());
        //payload.put("paymentChannel", generateJWTTokenRequest.getPaymentChannel());

        log.debug("payload to server={},", payload);
        return payload;
    }

    public HashMap<String, Object> makePayloadForPaymentInquiry(PaymentInquiry paymentRequest) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("paymentToken", paymentRequest.getPaymentToken());;
        payload.put("merchantID", paymentRequest.getMerchantID());
        payload.put("invoiceNo", paymentRequest.getInvoiceNo());
        return payload;
    }

}
