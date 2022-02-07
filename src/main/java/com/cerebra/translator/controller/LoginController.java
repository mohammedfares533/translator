package com.cerebra.translator.controller;

import com.cerebra.translator.controller.base.Controllable;
import com.cerebra.translator.dto.common.BaseResponse;
import com.cerebra.translator.dto.request.LoginRequestDTO;
import com.cerebra.translator.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/translator")
public class LoginController {


    private final LoginService loginService;

    public LoginController(@Autowired LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * This Endpoint For Validate User Info and Generate Token
     * @param requestLoginDTO : Contains user Information
     * @return : User Token If everything Is Okay
     */
    @PostMapping("/v1/api/login")
    @ResponseBody
    public ResponseEntity<BaseResponse> login(@RequestBody @Validated LoginRequestDTO requestLoginDTO) {

        String token = loginService.login(requestLoginDTO);

        return new ResponseEntity(Controllable.preparedResponse(token, "RESPONSE.OK"), HttpStatus.OK);
    }


}
