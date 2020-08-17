package com.gradle.multimodule.couponservice.entities;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Column;
import java.math.BigDecimal;
import javax.persistence.Id;

@Data
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="coupon_code")
    private String code;
    private BigDecimal discount;
    @Column(name="exp_date")
    private String expDate;
}
