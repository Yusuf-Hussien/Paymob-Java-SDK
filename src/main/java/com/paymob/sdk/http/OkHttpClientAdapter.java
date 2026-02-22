package com.paymob.sdk.http;

import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.core.auth.SecretKeyAuthStrategy;
import com.paymob.sdk.core.auth.BearerTokenAuthStrategy;
import com.paymob.sdk.exceptions.*;
import com.paymob.sdk.http.interceptors.AuthInterceptor;
import com.paymob.sdk.http.interceptors.RetryInterceptor;
import com.paymob.sdk.http.interceptors.LoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp implementation of HttpClient interface.
 * Provides connection pooling, timeouts, retry logic, and interceptors.
 */
public class OkHttpClientAdapter implements HttpClient {
    private OkHttpClient client;
    private ObjectMapper objectMapper;
    private String baseUrl;

    public OkHttpClientAdapter() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(new RetryInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    @Override
    public <T> T get(String endpoint, Class<T> responseClass, AuthStrategy authStrategy) {
        try {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(baseUrl + endpoint)
                    .get();

            applyAuth(requestBuilder, authStrategy);

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            return handleResponse(response, responseClass);
        } catch (IOException e) {
            throw new PaymobException("GET request failed", e);
        }
    }

    @Override
    public <T> T post(String endpoint, Object requestBody, Class<T> responseClass, AuthStrategy authStrategy) {
        try {
            RequestBody body;
            if (requestBody instanceof RequestBody) {
                body = (RequestBody) requestBody;
            } else {
                String jsonBody = objectMapper.writeValueAsString(requestBody);
                body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
            }

            Request.Builder requestBuilder = new Request.Builder()
                    .url(baseUrl + endpoint)
                    .post(body);

            applyAuth(requestBuilder, authStrategy);

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            return handleResponse(response, responseClass);
        } catch (IOException e) {
            throw new PaymobException("POST request failed", e);
        }
    }

    @Override
    public <T> T put(String endpoint, Object requestBody, Class<T> responseClass, AuthStrategy authStrategy) {
        try {
            RequestBody body;
            if (requestBody instanceof RequestBody) {
                body = (RequestBody) requestBody;
            } else {
                String jsonBody = objectMapper.writeValueAsString(requestBody);
                body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
            }

            Request.Builder requestBuilder = new Request.Builder()
                    .url(baseUrl + endpoint)
                    .put(body);

            applyAuth(requestBuilder, authStrategy);

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            return handleResponse(response, responseClass);
        } catch (IOException e) {
            throw new PaymobException("PUT request failed", e);
        }
    }

    @Override
    public <T> T delete(String endpoint, Class<T> responseClass, AuthStrategy authStrategy) {
        try {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(baseUrl + endpoint)
                    .delete();

            applyAuth(requestBuilder, authStrategy);

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            return handleResponse(response, responseClass);
        } catch (IOException e) {
            throw new PaymobException("DELETE request failed", e);
        }
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void setTimeout(int timeoutSeconds) {
        client = client.newBuilder()
                .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .build();
    }

    private void applyAuth(Request.Builder requestBuilder, AuthStrategy authStrategy) {
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

    private <T> T handleResponse(Response response, Class<T> responseClass) throws IOException {
        int statusCode = response.code();
        String responseBody = response.body() != null ? response.body().string() : "";

        if (statusCode >= 200 && statusCode < 300) {
            if (responseClass == String.class) {
                return (T) responseBody;
            }
            return objectMapper.readValue(responseBody, responseClass);
        } else {
            throw mapToException(statusCode, responseBody);
        }
    }

    private PaymobException mapToException(int statusCode, String responseBody) {
        switch (statusCode) {
            case 401:
                return new AuthenticationException("Authentication failed", statusCode, responseBody);
            case 404:
                return new ResourceNotFoundException("Resource not found", statusCode, responseBody);
            case 406:
                return new ValidationException("Validation failed", statusCode, responseBody);
            case 500:
            case 502:
            case 503:
            case 504:
                return new PaymobServerException("Server error", statusCode, responseBody);
            default:
                return new PaymobException("HTTP error: " + statusCode, statusCode, responseBody);
        }
    }
}
