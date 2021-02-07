package com.gradle.multimodule.couponservice.controller;

import com.gradle.multimodule.couponservice.entities.Coupon;
import com.gradle.multimodule.couponservice.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon-api")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @PostMapping("/coupon")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){

        return new ResponseEntity<>(couponRepository.save(coupon), HttpStatus.CREATED);
    }

    @GetMapping("/coupon/{code}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable String code){
        return new ResponseEntity<>(couponRepository.findByCode(code), HttpStatus.OK);
    }

    @GetMapping("/coupon")
    public ResponseEntity<List<Coupon>> getCoupons(){
        return new ResponseEntity<List<Coupon>>(couponRepository.findAll(), HttpStatus.OK);
    }
}
