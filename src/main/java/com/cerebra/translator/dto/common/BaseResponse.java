package com.cerebra.translator.dto.common;

import lombok.Data;

import java.util.Objects;

/**
 * error , response
 */
@Data
public final class BaseResponse {
    private final int flowCode;
    private final String message;
    private final Object body;

    public BaseResponse(int flowCode, String message, Object body) {
        this.flowCode = flowCode;
        this.message = message;
        this.body = body;
    }

    public int flowCode() {
        return flowCode;
    }

    public String message() {
        return message;
    }

    public Object body() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        BaseResponse that = (BaseResponse) obj;
        return this.flowCode == that.flowCode &&
                Objects.equals(this.message, that.message) &&
                Objects.equals(this.body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowCode, message, body);
    }

    @Override
    public String toString() {
        return "BaseResponse[" +
                "flowCode=" + flowCode + ", " +
                "message=" + message + ", " +
                "body=" + body + ']';
    }

}
