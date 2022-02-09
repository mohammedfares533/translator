package com.cerebra.translator.controller;

import com.cerebra.translator.DataBeanProvider;
import com.cerebra.translator.Endpoint;
import com.cerebra.translator.TranslatorAppApplicationTests;
import com.cerebra.translator.TranslatorApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest extends TranslatorAppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test Case For Customer Login Endpoint
     * @throws Exception
     */
    @Test
    void customerLogin() throws Exception {

        // Valid Case Of Customer Login  Endpoint
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoint.LOGIN_API.getUri())
                        .content(asJsonString(DataBeanProvider.validCustomerLoginRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").exists())
                .andExpect(jsonPath("$.body").isString())
                .andReturn();

        // InValid Case Of Customer Login Endpoint
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoint.LOGIN_API.getUri())
                        .content(asJsonString(DataBeanProvider.inValidCustomerLoginRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();


        // Valid Case Of Admin Login  Endpoint
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoint.LOGIN_API.getUri())
                        .content(asJsonString(DataBeanProvider.validAdminLoginRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").exists())
                .andExpect(jsonPath("$.body").isString())
                .andReturn();

        // InValid Case Of Admin Login Endpoint
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoint.LOGIN_API.getUri())
                        .content(asJsonString(DataBeanProvider.inValidAdminLoginRequestDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

    }
}