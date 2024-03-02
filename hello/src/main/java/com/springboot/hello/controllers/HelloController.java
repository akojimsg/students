package com.springboot.hello.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public Map<String, String> hello(){
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Hello World!");
        return response;
    }
}
