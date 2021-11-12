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

        String responsePayload = "{\"payload\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ3ZWJQYXltZW50VXJsIjoiaHR0cHM6Ly9zYW5kYm94LXBndy11aS4yYzJwLmNvbS9wYXltZW50LzQuMS8jL3Rva2VuL2tTQW9wczlad2hvczhoU1RTZUxUVWQ3JTJiOENvTkFEM2lFN0p1enpIOVpjNlhUWE9zVWV2aFlGJTJmS0F3S0U3QlRrYWNJNFJEUzQwTTIzSmtvZUxEbUt5N1BPcm4lMmY1STRCbWxDemkxRXdxdFU0JTNkIiwicGF5bWVudFRva2VuIjoia1NBb3BzOVp3aG9zOGhTVFNlTFRVZDcrOENvTkFEM2lFN0p1enpIOVpjNlhUWE9zVWV2aFlGL0tBd0tFN0JUa2FjSTRSRFM0ME0yM0prb2VMRG1LeTdQT3JuLzVJNEJtbEN6aTFFd3F0VTQ9IiwicmVzcENvZGUiOiIwMDAwIiwicmVzcERlc2MiOiJTdWNjZXNzIn0.XGoTRAEhaCytQ9DPjXxsGw7vNZJUanXl-Z7r1u25hVY\"}";
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
