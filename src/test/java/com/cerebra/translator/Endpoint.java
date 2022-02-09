package com.cerebra.translator;

public enum Endpoint {
    LOGIN_API("/translator/v1/api/login");

    private String uri;

    Endpoint(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
