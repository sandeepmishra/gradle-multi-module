package com.gradle.multimodule.productservice.controller;


import com.gradle.multimodule.productservice.dto.Coupon;
import com.gradle.multimodule.productservice.entity.Product;
import com.gradle.multimodule.productservice.repository.ProductRepository;
import com.gradle.multimodule.productservice.restclient.CouponClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product-api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    CouponClient couponClient;

    @HystrixCommand(fallbackMethod = "handleCouponFailure")
    @PostMapping("/product/{couponCode}")
    public Product createProduct(@RequestBody Product product, @PathVariable String couponCode){
        Coupon coupon = couponClient.getCoupon(couponCode);
        product.setProductPrice(product.getProductPrice().subtract(coupon.getDiscount()));
        return productRepository.save(product);
    }

    private Product handleCouponFailure(Product product, String couponCode){
        return product;
    }
    @GetMapping("/product/{id}")
    public Product getProduct(Long id){
        return  productRepository.findById(id).get();
    }
}
