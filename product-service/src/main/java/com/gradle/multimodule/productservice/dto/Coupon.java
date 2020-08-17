package com.gradle.multimodule.productservice.dto;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
public class Coupon {

    private Long id;
    private String code;
    private BigDecimal discount;
    private String expDate;
}
