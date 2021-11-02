package com.test.demo2c2p.api.response;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RestResponse {
    private boolean success;
    private String message;
    private Map<String, String> errors = new HashMap<>();

    public static RestResponse success() {
        RestResponse response = new RestResponse();
        response.success = true;
        return response;
    }

    public static RestResponse error() {
        RestResponse response = new RestResponse();
        response.success = false;
        return response;
    }

    public RestResponse addError(String fieldName, String message) {
        this.errors.put(fieldName, message);
        return this;
    }

    public RestResponse message(String message) {
        this.setMessage(message);
        return this;
    }
}

