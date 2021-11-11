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

    public HttpsURLConnection getConnection() throws Exception {
        URL obj = new URL(endPoint);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/*+json");
        con.setRequestProperty("Accept", "text/plain");
        con.setDoOutput(true);

        return con;
    }

    public String getResponse(JSONObject requestData, HttpsURLConnection con) throws IOException {
        StringBuffer response = new StringBuffer();

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

            wr.writeBytes(requestData.toString());
            wr.flush();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            log.debug("response={}", response);
        }

        return response.toString();
    }
}
