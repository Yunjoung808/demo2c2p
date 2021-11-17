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
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

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

    public String sendPaymentActionAPIRequest(String payload) throws Exception {
        return getPaymentActionConnection(payload,"https://demo2.2c2p.com/2C2PFrontend/PaymentActionV2/PaymentProcess.aspx");
    }


    public String doPayment(String paymentToken,String channelCode) throws Exception {
        log.info("=========================about to start payment ==============");
        JSONObject requestData = new JSONObject();
        log.debug(paymentToken);
        requestData.put("paymentToken",paymentToken.substring(1,paymentToken.length()-1));

        JSONObject customer = new JSONObject();
        customer.put("channelCode",channelCode);
        //customer.put("channelCode","TRUEMONEY");
        
        //requestData.put("clientID","S125KJH3ITF323A5S6725134267F4SD2");
        //requestData.put("clientID","E380BEC2BFD727A4B6845133519F3AD7");

        JSONObject data = new JSONObject();
        data.put("name","sky");
        data.put("email","sky@eximbay.com");
        data.put("mobileNo","+660008683501");
        JSONObject payment = new JSONObject();
        payment.put("code",customer);
        payment.put("data",data);
        requestData.put("payment",payment);

        log.debug("requestData ={}",requestData);
        return getConnection(requestData,"https://sandbox-pgw.2c2p.com/payment/4.1/Payment");
        
    }

    public String doCancellation (String paymentToken, String clientID) throws Exception {
        log.info("=========================about to start payment ==============");
        JSONObject requestData = new JSONObject();
        log.debug(paymentToken);
        requestData.put("paymentToken",paymentToken);
        requestData.put("clientID",clientID);
        log.debug("requestData ={}",requestData);
        return getConnection(requestData,"https://sandbox-pgw.2c2p.com/payment/4.1/canceltransaction");
        
    }


    private String getPaymentActionConnection(String payload,String endPoint) throws Exception {
        try
            {
            String urlParameters = "paymentRequest=" + payload;
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            URL obj = new URL(endPoint);
            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);


            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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

    public String getConnection(JSONObject requestData,String endPoint) throws Exception {
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
