package com.cerebra.translator.exception;


import com.cerebra.translator.config.ApplicationResourceBundle;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * This Class for Fill Error Date To Customer
 */
@Data
public class ApiException extends RuntimeException {

    private  HttpStatus httpStatus;

    private  String message;


    /**
     * Constructor For Return Status and localized Massage
     * @param httpStatus
     * @param message
     */
    public ApiException(HttpStatus httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.message = ApplicationResourceBundle.getString(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
