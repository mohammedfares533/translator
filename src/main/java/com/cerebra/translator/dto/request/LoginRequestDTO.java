package com.cerebra.translator.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * User Login API Request
 */
@Data
public class LoginRequestDTO {

    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    private Boolean isAdmin;
}
