package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping(value = "/test")
    public @ResponseBody String test(){
        return "@RespsonseBody를 추가하면 jsp가 보이는게 아니라 내용 자체가 보인다.";
    }
    
}
