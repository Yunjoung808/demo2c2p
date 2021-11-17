package com.test.demo2c2p.controller;

import com.test.demo2c2p.api.response.RestResponse;


import com.test.demo2c2p.api.request.GenerateJWTTokenRequest;
import com.test.demo2c2p.service.Demo2c2pService;
import com.test.demo2c2p.api.response.RedirectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class RedirectController {

    @RequestMapping("/redirected")
    public String returnconf(Model model) throws Exception{
        model.addAttribute("wut","wut");
        return "success";
    }

    @RequestMapping("/frontendredirect")
    public String returnconfs() throws Exception{
        return "success";
    }
}
