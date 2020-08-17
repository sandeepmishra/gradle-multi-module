package com.gradle.multimodule.productservice.restclient;


import com.gradle.multimodule.productservice.dto.Coupon;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("zuul-proxy-gateway")
//@RibbonClient("COUPON-SERVICE")
public interface CouponClient {

    @GetMapping("coupon-service/coupon-api/coupon/{code}")
    Coupon getCoupon(@PathVariable("code") String code);
}
