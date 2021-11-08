package com.test.demo2c2p.service;

import com.test.demo2c2p.dto.Request2c2pDto;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.interfaces.*;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

@Service
public class Demo2c2pService {

    public void generateJWTToken(Request2c2pDto request2c2pDto){
        
        String token="";
        String secretKey = "ECC4E54DBA738857B84A7EBC6B5DC7187B8DA68750E88AB53AAA41F548D6F2D9";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("merchantID",request2c2pDto.getMerchantID());
        payload.put("invoiceNo",request2c2pDto.getInvoiceNo());
        payload.put("description",request2c2pDto.getDescription());
        payload.put("amount",request2c2pDto.getAmount());
        payload.put("currencyCode",request2c2pDto.getCurrencyCode());
        payload.put("paymentChannel","CC");
        

        System.out.println(payload);

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            token = JWT.create().withPayload(payload).sign(algorithm);           

          } catch (JWTCreationException | IllegalArgumentException e){
            //Invalid Signing configuration / Couldn't convert Claims.
            e.printStackTrace();
          }

          JSONObject requestData = new JSONObject();
            requestData.put("payload", token);

            System.out.println(requestData);

            try{
              String endpoint = "https://sandbox-pgw.2c2p.com/payment/4.1/PaymentToken";
              URL obj = new URL(endpoint);
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

              System.out.println("response::"+response); 

              in.close();
            }catch(Exception e){
              e.printStackTrace();
            }
          
          System.out.println(requestData);

          processJWTToken(requestData);
          System.out.println("==========END :: generateJWTToken ===============");

    }

    private void processJWTToken(JSONObject requestData){ 
      System.out.println("==========START :: processJWTToken ===============");

      try{
        String responsePayload = "{\"payload\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ3ZWJQYXltZW50VXJsIjoiaHR0cHM6Ly9zYW5kYm94LXBndy11aS4yYzJwLmNvbS9wYXltZW50LzQuMS8jL3Rva2VuL2tTQW9wczlad2hvczhoU1RTZUxUVWNKMFVRaVZhYTZ2Qmk1YXo5UWlmRUUlMmJSZDY1Y00zZE55ZjRXNWFZVmlxemthajVzTGRUbW9lSSUyYjAyMSUyZllyb0tEYjRSbVZvcWc4YVAlMmJoT0VKRDB0JTJiZyUzZCIsInBheW1lbnRUb2tlbiI6ImtTQW9wczlad2hvczhoU1RTZUxUVWNKMFVRaVZhYTZ2Qmk1YXo5UWlmRUUrUmQ2NWNNM2ROeWY0VzVhWVZpcXprYWo1c0xkVG1vZUkrMDIxL1lyb0tEYjRSbVZvcWc4YVAraE9FSkQwdCtnPSIsInJlc3BDb2RlIjoiMDAwMCIsInJlc3BEZXNjIjoiU3VjY2VzcyJ9.0YQthKwZEjR9giHWc3mkce9ngQnCNi0asXFWPHP_81k\"}";
        JSONParser parser = new JSONParser();
        JSONObject responseJSON = (JSONObject) parser.parse(responsePayload);
        String responseToken = responseJSON.get("payload").toString();

        String secretKey = "ECC4E54DBA738857B84A7EBC6B5DC7187B8DA68750E88AB53AAA41F548D6F2D9"; 

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
          .build();          
        verifier.verify(responseToken);   //verify signature
      
        DecodedJWT jwt = JWT.decode(responseToken); //decode encoded payload    
        Map<String, Claim> responseData = jwt.getClaims();
      
        String paymentToken = responseData.get("paymentToken").toString();

        System.out.println("responseData::"+responseData);

      }catch (JWTVerificationException e) {
        //Invalid signature/claims
        e.printStackTrace();
      }catch(Exception e)
      {
        e.printStackTrace();
      }

      System.out.println("==========END :: processJWTToken ===============");

    }

}
