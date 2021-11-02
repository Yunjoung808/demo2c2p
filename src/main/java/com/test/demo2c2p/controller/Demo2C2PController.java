package com.test.demo2c2p.controller;

import com.test.demo2c2p.api.response.RestResponse;
import com.test.demo2c2p.dto.Request2c2pDto;
import com.test.demo2c2p.service.Demo2c2pService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/demo2c2p")
public class Demo2C2PController {
    private final Demo2c2pService demo2c2pService;

    
    @PostMapping("/generateJWTToken")
    public ResponseEntity<RestResponse> requestTo2c2p(@RequestBody Request2c2pDto request2c2pDto){
        
        demo2c2pService.generateJWTToken(request2c2pDto);

        return ResponseEntity.ok(RestResponse.success());
    }
    
}
