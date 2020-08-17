package com.gradle.multimodule.couponservice.controller;

import com.gradle.multimodule.couponservice.entities.Coupon;
import com.gradle.multimodule.couponservice.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon-api")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @PostMapping("/coupon")
    public Coupon createCoupon(@RequestBody Coupon coupon){

        return couponRepository.save(coupon);
    }

    @GetMapping("/coupon/{code}")
    public Coupon getCoupon(@PathVariable String code){
        System.out.println("Server running on 8083");
        return couponRepository.findByCode(code);
    }
}
