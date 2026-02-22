package com.paymob.sdk.http.interceptors;

import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.core.auth.SecretKeyAuthStrategy;
import com.paymob.sdk.core.auth.BearerTokenAuthStrategy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

/**
 * Interceptor for applying authentication to all requests.
 * Handles both Secret Key and Bearer Token authentication strategies.
 */
public class AuthInterceptor implements Interceptor {
    private AuthStrategy authStrategy;

    public AuthInterceptor() {
        // Default constructor - auth will be set per request
    }

    public AuthInterceptor(AuthStrategy authStrategy) {
        this.authStrategy = authStrategy;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();

        // Apply authentication if strategy is available
        if (authStrategy != null) {
            if (authStrategy instanceof SecretKeyAuthStrategy) {
                SecretKeyAuthStrategy secretAuth = (SecretKeyAuthStrategy) authStrategy;
                requestBuilder.header("Authorization", secretAuth.getAuthorizationHeader());
            } else if (authStrategy instanceof BearerTokenAuthStrategy) {
                BearerTokenAuthStrategy bearerAuth = (BearerTokenAuthStrategy) authStrategy;
                String authorizationHeader = bearerAuth.getAuthorizationHeader();
                if (authorizationHeader != null) {
                    requestBuilder.header("Authorization", authorizationHeader);
                }
            }
        }

        Request authenticatedRequest = requestBuilder.build();
        return chain.proceed(authenticatedRequest);
    }

    public void setAuthStrategy(AuthStrategy authStrategy) {
        this.authStrategy = authStrategy;
    }
}
