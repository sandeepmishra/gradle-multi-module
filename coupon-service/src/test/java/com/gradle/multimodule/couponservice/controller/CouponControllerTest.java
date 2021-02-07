package com.gradle.multimodule.couponservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.multimodule.couponservice.entities.Coupon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGettALlCoupons() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        Coupon coupon = new Coupon();
        coupon.setCode("KJH8453");
        coupon.setDiscount(new BigDecimal("35"));
        coupon.setExpDate("20-02-2021");
        mockMvc.perform(MockMvcRequestBuilders.post("/coupon-api/coupon")
                .content(objectMapper.writeValueAsString(coupon)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());

        MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/coupon-api/coupon").contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
