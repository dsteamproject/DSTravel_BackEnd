package com.example.controller;

import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.repository.TDRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    TDRepository tdRepository;

    @GetMapping(value = "/home")
    public Map<String, Object> homeGET(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
