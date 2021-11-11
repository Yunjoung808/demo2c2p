package com.test.demo2c2p.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    @Value("${demo2c2p.jwt.token.secretKey}")
    private String secretKey;

    public String getToken(HashMap<String, Object> payload) {
        return JWT.create().withPayload(payload).sign(getAlgorithm());
    }

    public void process() throws Exception {
        log.info("==========START :: processJWTToken ===============");

        String responsePayload = "{\"payload\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ3ZWJQYXltZW50VXJsIjoiaHR0cHM6Ly9zYW5kYm94LXBndy11aS4yYzJwLmNvbS9wYXltZW50LzQuMS8jL3Rva2VuL2tTQW9wczlad2hvczhoU1RTZUxUVWNKMFVRaVZhYTZ2Qmk1YXo5UWlmRUUlMmJSZDY1Y00zZE55ZjRXNWFZVmlxemthajVzTGRUbW9lSSUyYjAyMSUyZllyb0tEYjRSbVZvcWc4YVAlMmJoT0VKRDB0JTJiZyUzZCIsInBheW1lbnRUb2tlbiI6ImtTQW9wczlad2hvczhoU1RTZUxUVWNKMFVRaVZhYTZ2Qmk1YXo5UWlmRUUrUmQ2NWNNM2ROeWY0VzVhWVZpcXprYWo1c0xkVG1vZUkrMDIxL1lyb0tEYjRSbVZvcWc4YVAraE9FSkQwdCtnPSIsInJlc3BDb2RlIjoiMDAwMCIsInJlc3BEZXNjIjoiU3VjY2VzcyJ9.0YQthKwZEjR9giHWc3mkce9ngQnCNi0asXFWPHP_81k\"}";
        JSONParser parser = new JSONParser();
        JSONObject responseJSON = (JSONObject) parser.parse(responsePayload);
        String responseToken = responseJSON.get("payload").toString();

        //verify signature
        verifyToken(responseToken);

        //decode encoded payload
        Map<String, Claim> responseData = getDecodedJWT(responseToken);

        String paymentToken = responseData.get("paymentToken").toString();

        log.debug("responseData={}", responseData);
        log.debug("paymentToken={}", paymentToken);
        log.info("==========END :: processJWTToken ===============");
    }

    private Map<String, Claim> getDecodedJWT(String responseToken) {
        DecodedJWT jwt = JWT.decode(responseToken);
        return jwt.getClaims();
    }

    private void verifyToken(String responseToken) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        verifier.verify(responseToken);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }
}
