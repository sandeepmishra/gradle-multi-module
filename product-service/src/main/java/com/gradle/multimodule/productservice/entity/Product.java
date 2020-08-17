package com.gradle.multimodule.productservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(name="coupon_code")
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    //private String couponCode;

}
