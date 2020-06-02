package com.smartparkinglot.backendservice.config;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstraints {
    public static final String SIGN_UP_URL = "/api/v1/users/sign-up";
    public static final String LOGIN_URL = "/api/v1/users/authenticate";
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_TYPE = "Authorization";

}
