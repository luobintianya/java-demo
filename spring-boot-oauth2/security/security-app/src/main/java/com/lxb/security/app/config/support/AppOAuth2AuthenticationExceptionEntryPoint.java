package com.lxb.apibrowser.security.app.config.support;

import com.lxb.apibrowser.security.core.support.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
* @Author: lixubin
* @Date: 2019-08-20
* @Description:
*/
@Component
public class AppOAuth2AuthenticationExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {

    private static final String UNAUTHORIZED = "Full authentication is required to access this resource";

    private static final String ACCESS_TOKEN_EXPIRED = "Access token expired";

    private static final String REQUIRE_LOGIN = "Require login";

    @Override
    protected ResponseEntity<Response> enhanceResponse(ResponseEntity<?> response, Exception exception) {
        String errorMessage;
        if (exception != null) {
            Throwable cause = exception.getCause();
            if (cause instanceof InvalidTokenException) {
                errorMessage = ACCESS_TOKEN_EXPIRED;
            } else {
                errorMessage = exception.getMessage();
            }

            if (UNAUTHORIZED.equals(errorMessage)) {
                errorMessage = REQUIRE_LOGIN;
            }
        } else {
            errorMessage = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        }

        Response newResponse = new Response()
                .setSuccess(false)
                .setMessage(errorMessage)
                .setStatus(response.getStatusCodeValue());
        return ResponseEntity
                .status(response.getStatusCodeValue())
                .headers(response.getHeaders())
                .body(newResponse);
    }

}
