package com.test.demo2c2p.controller;

import com.test.demo2c2p.api.response.RestResponse;

import com.test.demo2c2p.api.request.CancelRequest;
import com.test.demo2c2p.api.request.GenerateJWTTokenRequest;
import com.test.demo2c2p.api.request.PaymentInquiry;
import com.test.demo2c2p.api.request.PaymentActionRequest;
import com.test.demo2c2p.service.Demo2c2pService;
import com.test.demo2c2p.api.response.RedirectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


@Controller
@RequiredArgsConstructor
@RequestMapping("/demo2c2p")
public class Demo2C2PController {

    private final Demo2c2pService demo2c2pService;

    @PostMapping("/generateJWTToken")
    public ResponseEntity<RestResponse> requestTo2c2p(@RequestBody GenerateJWTTokenRequest generateJWTTokenRequest) throws Exception {
        String data = demo2c2pService.generateJWTToken(generateJWTTokenRequest);
        URI redirectUri = new URI(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        URL url = new URL(data);
        URLConnection conn = url.openConnection(); 
        return new ResponseEntity<>(headers,HttpStatus.OK);
        // String newurl = data.substring(1,data.length()-1);
        // URI redirectUri = new URI(newurl);
        // System.out.println("redirectUri");
        // HttpHeaders headers = new HttpHeaders();
        // headers.setLocation(redirectUri);
        // return new ResponseEntity<>(headers,HttpStatus.OK);


    }

    @PostMapping("/paymentconfirmation")
    public ResponseEntity<RestResponse> redirectPayment(@RequestBody RedirectResponse redirectResponse) throws Exception{
        demo2c2pService.parseDetails(redirectResponse);

        URI redirectUri = new URI("/redirected");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        System.out.println("im here");
        return new ResponseEntity<>(headers,HttpStatus.OK);
    }
    @PostMapping("/cancel")
    public ResponseEntity<RestResponse> cancelPayment(@RequestBody CancelRequest cancelRequest) throws Exception{
        String result = demo2c2pService.sendCancelRequest(cancelRequest);
        System.out.println(result);
        URI redirectUri = new URI("/redirected");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        System.out.println("im here");
        return new ResponseEntity<>(headers,HttpStatus.OK);
    }

    @PostMapping("/paymentInquiry")
    public ResponseEntity<RestResponse> payemntInquiry(@RequestBody PaymentInquiry paymentRequest) throws Exception{
        System.out.println(paymentRequest);
        String result = demo2c2pService.doPaymentInquiry(paymentRequest);
        System.out.println("paymentInquiryResult::"+result);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/inquiry")
    public ResponseEntity<RestResponse> getInquiry(@RequestBody PaymentActionRequest paymentActionRequest) throws Exception{
        System.out.println("i'm heree");
        HashMap<String,String> result = demo2c2pService.sendPaymentActionRequest(paymentActionRequest);
        HttpHeaders headers = new HttpHeaders();
        if (result.get("respCode").equals("00")){
            URI redirectUri = new URI("/request_success.html");
            headers.setLocation(redirectUri);
        }
        else{
            URI redirectUri = new URI("/request_fail.html");
            headers.setLocation(redirectUri);
        }
        
        return new ResponseEntity<>(headers,HttpStatus.OK);
    }

/*
    @PostMapping("/paymentconfirmation")
    public ModelAndView redirectPayment(@RequestBody RedirectResponse redirectResponse) throws Exception{
        demo2c2pService.parseDetails(redirectResponse);

        HttpHeaders headers = new HttpHeaders();
        return new ModelAndView("redirect:/redirected");
    }
    */

    @PostMapping("/redirected")
    public ModelAndView whaaaaa() throws Exception{
        System.out.println("i'm hereasss");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("cancel");
        return mv;
    }





    

}
