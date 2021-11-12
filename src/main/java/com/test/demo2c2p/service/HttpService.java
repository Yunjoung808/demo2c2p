package com.test.demo2c2p.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Slf4j
@Service
public class HttpService {

    @Value("${demo2c2p.endPoint}")
    private String endPoint;

    public String sendRequest(String token) throws Exception {
        JSONObject requestData = new JSONObject();
        requestData.put("payload", token);
        log.debug("requestData={}", requestData);
        return getConnection(requestData,endPoint);
        
    }

    public String doPayment(String paymentToken) throws Exception {
        log.info("=========================about to start payment ==============");
        JSONObject requestData = new JSONObject();
        log.debug(paymentToken);
        requestData.put("paymentToken",paymentToken.substring(1,paymentToken.length()-1));

        JSONObject customer = new JSONObject();
        customer.put("channelCode","GRAB");
        
        JSONObject data = new JSONObject();
        data.put("name","Terrance");
        data.put("email","terrance.tay@2c2p.com");

        JSONObject payment = new JSONObject();
        payment.put("code",customer);
        payment.put("data",data);
        requestData.put("payment",payment);

        log.debug("requestData ={}",requestData);
        return getConnection(requestData,"https://sandbox-pgw.2c2p.com/payment/4.1/Payment");
        
    }

    private String send(HttpsURLConnection con, JSONObject requestData) throws IOException {
        StringBuffer responseData = new StringBuffer();

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

            wr.writeBytes(requestData.toString());
            wr.flush();
            wr.close();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseData.append(inputLine);
            }
            in.close();
            log.debug("responseData={}", responseData);
        }

        return responseData.toString();
    }

    private String getConnection(JSONObject requestData,String endPoint) throws Exception {
        try
            {
            URL obj = new URL(endPoint);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/*+json");
            con.setRequestProperty("Accept", "text/plain");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(requestData.toString());
            wr.flush();
            wr.close();


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();

            }catch(Exception e){
            e.printStackTrace();
            }
        return "error";
    }
}
