package com.test.demo2c2p.controller;

import com.test.demo2c2p.api.response.RestResponse;


import com.test.demo2c2p.api.request.GenerateJWTTokenRequest;
import com.test.demo2c2p.service.Demo2c2pService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


@Controller
@RequiredArgsConstructor
@RequestMapping("/demo2c2p")
public class Demo2C2PController {

    private final Demo2c2pService demo2c2pService;

    @PostMapping("/generateJWTToken")
    public ResponseEntity<RestResponse> requestTo2c2p(@RequestBody GenerateJWTTokenRequest generateJWTTokenRequest) throws Exception {
        String data = demo2c2pService.generateJWTToken(generateJWTTokenRequest);
        /*URI redirectUri = new URI(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        URL url = new URL(data);
        URLConnection conn = url.openConnection(); 
        */
        URI redirectUri = new URI(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers,HttpStatus.OK);
    }



    

}
