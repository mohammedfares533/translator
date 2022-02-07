package com.cerebra.translator.controller.base;

import com.cerebra.translator.config.ApplicationResourceBundle;
import com.cerebra.translator.dto.common.BaseResponse;
import com.cerebra.translator.dto.common.ErrorModel;


import java.util.Locale;

/**
 * Controllable helper class used to prepared response and error response
 */
public abstract class Controllable {

    Controllable() {
    }

    public static BaseResponse preparedResponse(Object body,
                                                String message) {

        return preparedResponse(body, message, 0);
    }

    public static BaseResponse preparedResponse(Object body,
                                                String message, int flowCode) {

        return new BaseResponse(flowCode, ApplicationResourceBundle.getString(message), body);
    }

    public static ErrorModel preparedErrorResponse(String message, Locale locale) {

        return new ErrorModel(null, ApplicationResourceBundle.getString(message));
    }
}
