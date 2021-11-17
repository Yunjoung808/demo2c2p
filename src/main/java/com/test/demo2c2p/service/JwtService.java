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


    public String process(String responsePayload) throws Exception {
        log.info("==========START :: processJWTToken ===============");

        JSONParser parser = new JSONParser();
        JSONObject responseJSON = (JSONObject) parser.parse(responsePayload);
        String responseToken = responseJSON.get("payload").toString();

        //verify signature
        verifyToken(responseToken);

        //decode encoded payload
        Map<String, Claim> responseData = getDecodedJWT(responseToken);
        log.debug("responseData={}",responseData);
        String paymentToken = responseData.get("paymentToken").toString();
        String webPaymentUrl = responseData.get("webPaymentUrl").toString();
        return webPaymentUrl;

        // log.debug("paymentToken={}", paymentToken);
        // log.debug("webPaymentUrl={}", webPaymentUrl);
        // log.info("==========END :: processJWTToken ===============");
        // return paymentToken;
    }

    public Map<String, Claim> getDecodedJWT(String responseToken) {
        DecodedJWT jwt = JWT.decode(responseToken);
        return jwt.getClaims();
    }

    public Map<String, Claim> getDecodedPayload(String responseToken) {
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
