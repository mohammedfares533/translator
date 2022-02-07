package com.cerebra.translator.controller.advisor;

import com.cerebra.translator.controller.base.Controllable;
import com.cerebra.translator.dto.common.BaseResponse;
import com.cerebra.translator.dto.common.ErrorModel;
import com.cerebra.translator.exception.ApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@ControllerAdvice
/**
 * Class Handle  Exceptions
 */
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE_NAME = "ERROR.RESPONSE";


    /**
     * Handle Api  Exception
     *
     * @param error
     * @param request
     * @return
     */
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<BaseResponse> handleCustomError(ApiException error, WebRequest request) {

        return new ResponseEntity<BaseResponse>(Controllable.preparedResponse(
                Controllable.preparedErrorResponse(error.getMessage(), request.getLocale()), ERROR_MESSAGE_NAME),
                error.getHttpStatus());
    }

    /**
     * Use This Method For Handle Not Valid User Arguments
     *
     * @param ex      : Represent Exception Object
     * @param headers : Represent Request Header
     * @param status  : Represent Response Status
     * @param request : Represent User Request
     * @return Custom Exception Massage To Till User Which Argument Is In valid
     */
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<ErrorModel> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(Controllable.preparedErrorResponse(error.getDefaultMessage(), request.getLocale()));
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(Controllable.preparedErrorResponse(error.getDefaultMessage(), request.getLocale()));
        }

        return new ResponseEntity<>(Controllable.preparedResponse(errors, ERROR_MESSAGE_NAME), HttpStatus.BAD_REQUEST);
    }
}
