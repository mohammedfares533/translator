package com.cerebra.translator.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.cerebra.translator.dto.request.LoginRequestDTO;
import com.cerebra.translator.exception.ApiException;
import com.cerebra.translator.model.SystemUser;
import com.cerebra.translator.model.enums.Roles;
import com.cerebra.translator.model.enums.Status;
import com.cerebra.translator.repository.UserRepository;
import com.cerebra.translator.security.JwtTokenProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class LoginService {


    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    public LoginService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }


    /**
     * Use This Method for check user Account validity and generate token
     * @param requestLoginDTO : represent User Info
     * @return Token For User Or Exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String login(LoginRequestDTO requestLoginDTO) {
        SystemUser user;
        String token;
        try {
            // check if User Account Exist
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestLoginDTO.getUsername(),
                    requestLoginDTO.getPassword()));
        } catch (Exception e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "USERNAME_OR_PASSWORD_WRONG");
        }
        // check User Type
        if (requestLoginDTO.getIsAdmin()) {
            user = userRepository.findSystemUserByUserNameAndRoleIs(requestLoginDTO.getUsername(), Roles.ADMIN);
        } else {
            user = userRepository.findSystemUserByUserNameAndRoleIs(requestLoginDTO.getUsername(), Roles.USER);
        }
        // check if User Account Exist
        if (user == null || user.getRole() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "USERNAME_OR_PASSWORD_WRONG");
        }
        // check if User Account Is Active
        if (!user.getStatus().equals(Status.ACTIVE)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "USER_IN_ACTIVE");
        }


        // Generate Token and Return Token
        try {
            token = jwtTokenProvider.createToken(requestLoginDTO.getUsername(),
                    user.getId().toString(),
                    user.getRole().name());
        } catch (Exception exception) {
            log.error("something went wrong when generate token with error :");
            exception.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR.MESSAGE");
        }

        return token;
    }
}
