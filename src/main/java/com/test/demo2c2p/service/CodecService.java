package com.test.demo2c2p.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Hex;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

@Service
public class CodecService {

    private Encoder encoder = Base64.getEncoder();
    private Decoder decoder = Base64.getDecoder(); 

    @Value("${demo2c2p.jwt.token.secretKey}")
    private String secretKey;
    public String hashHMAC(String toHash){
        try{
            byte[] keyBytes = secretKey.getBytes();      
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(toHash.getBytes());
            byte[] hexBytes = new Hex().encode(rawHmac);
            return new String(hexBytes, "UTF-8").toUpperCase();
    
        }
        catch (Exception e){
            e.printStackTrace();
            return "No Algorithm Found";
        }

    }   

    public String encodeString (String toEncode){
        byte[] encodedBytes = encoder.encode(toEncode.getBytes());
        return new String(encodedBytes);
    }

    public String decodeString(String toDecode){
        byte[] decodedBytes = decoder.decode(toDecode);
        return new String(decodedBytes);
    }



}
