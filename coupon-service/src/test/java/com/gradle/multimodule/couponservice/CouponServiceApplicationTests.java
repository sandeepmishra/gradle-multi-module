package com.gradle.multimodule.couponservice;

import com.gradle.multimodule.couponservice.controller.CouponController;
import com.gradle.multimodule.couponservice.repository.CouponRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CouponController.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
//@WebMvcTest(CouponController.class)
public class CouponServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;
//    @Test
//    void contextLoads() {
//    }

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void testGettALlCoupons() throws Exception{

//        mockMvc.perform(MockMvcRequestBuilders.post("/library-event")
//                .content(objectMapper.writeValueAsString(libraryEvent)).contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isCreated());

        //MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get("/coupon").contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk());

        //System.out.println(result.getResponse().getContentAsString());
    }

}
