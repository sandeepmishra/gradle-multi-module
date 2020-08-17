package com.gradle.multimodule.userservices;

import com.gradle.multimodule.userservices.domain.Addition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class UserServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServicesApplication.class, args);
    }

    @Autowired
    @GetMapping("/greeting/{user}")
    public String getGreeting(@PathVariable("user") String user){
        return "Hello "+user;
    }

    @GetMapping("/getproject")
    public String getProjectFrom(){
        return "From user services.";
    }


    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/getaddition")
    public String getAddition(@RequestBody Addition addition){
        System.out.println(addition.toString());
        ApplicationContext ctx = new ClassPathXmlApplicationContext();
        DefaultSingletonBeanRegistry defaultSingletonBeanRegistry;
        return String.valueOf(addition.getVal1()+addition.getVal2()+addition.getVal3()+addition.getVal4());
    }
}
