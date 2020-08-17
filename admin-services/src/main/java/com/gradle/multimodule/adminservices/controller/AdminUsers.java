package com.gradle.multimodule.adminservices.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUsers {

    @GetMapping("/getproject")
    public String getProjectFrom(){
        return "From admin services.";
    }
}
