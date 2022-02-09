package com.cerebra.translator;

import com.cerebra.translator.dto.request.LoginRequestDTO;

public class DataBeanProvider {

    // Valid Scenario For Login Customer
    public static LoginRequestDTO validCustomerLoginRequestDto() {

        LoginRequestDTO customerLoginRequestDto = new LoginRequestDTO("user","user123",false);
        return customerLoginRequestDto;
    }

    // Valid Scenario For Login Admin
    public static LoginRequestDTO validAdminLoginRequestDto() {

        LoginRequestDTO customerLoginRequestDto = new LoginRequestDTO("admin","admin123",true);
        return customerLoginRequestDto;
    }

    // In Valid Scenario For Login Customer
    public static LoginRequestDTO inValidCustomerLoginRequestDto() {

        LoginRequestDTO customerLoginRequestDto = new LoginRequestDTO("moh123456","0789mmm",false);
        return customerLoginRequestDto;
    }


    // In Valid Scenario For Login Admin
    public static LoginRequestDTO inValidAdminLoginRequestDto() {

        LoginRequestDTO customerLoginRequestDto = new LoginRequestDTO("moh1234","0789m",true);
        return customerLoginRequestDto;
    }
}
