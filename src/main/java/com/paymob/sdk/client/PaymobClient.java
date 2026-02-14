package com.paymob.sdk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymob.sdk.Paymob;
import com.paymob.sdk.exceptions.PaymobApiException;
import com.paymob.sdk.exceptions.PaymobException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PaymobClient {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) {
        return sendRequest(endpoint, "POST", requestBody, responseType);
    }

    public <T> T get(String endpoint, Class<T> responseType) {
        return sendRequest(endpoint, "GET", null, responseType);
    }

    public <T> T postWithSecretKey(String endpoint, Object requestBody, Class<T> responseType) {
        return sendRequestWithSecretKey(endpoint, "POST", requestBody, responseType);
    }

    public <T> T getWithSecretKey(String endpoint, Class<T> responseType) {
        return sendRequestWithSecretKey(endpoint, "GET", null, responseType);
    }

    public <T> T getWithBearerToken(String endpoint, String token, Class<T> responseType) {
        return executeRequest(endpoint, "GET", null, responseType, false, token);
    }

    private <T> T sendRequest(String endpoint, String method, Object requestBody, Class<T> responseType) {
        return executeRequest(endpoint, method, requestBody, responseType, false, null);
    }

    private <T> T sendRequestWithSecretKey(String endpoint, String method, Object requestBody, Class<T> responseType) {
        return executeRequest(endpoint, method, requestBody, responseType, true, null);
    }

    private <T> T executeRequest(String endpoint, String method, Object requestBody, Class<T> responseType,
            boolean useSecretKey, String bearerToken) {
        try {
            String url = Paymob.getBaseUrl() + endpoint;
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(Paymob.getTimeoutSeconds()));

            if (useSecretKey) {
                String secret = Paymob.getSecretKey();
                if (secret == null) {
                    throw new PaymobException("Secret Key is not initialized. Call Paymob.init() with Secret Key.");
                }
                builder.header("Authorization", "Token " + secret);
            } else if (bearerToken != null) {
                builder.header("Authorization", "Bearer " + bearerToken);
            }

            if (requestBody != null) {
                String json = objectMapper.writeValueAsString(requestBody);
                builder.method(method, HttpRequest.BodyPublishers.ofString(json));
            } else {
                if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                    builder.method(method, HttpRequest.BodyPublishers.noBody());
                } else {
                    builder.method(method, HttpRequest.BodyPublishers.noBody());
                }
            }

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode >= 200 && statusCode < 300) {
                return objectMapper.readValue(responseBody, responseType);
            } else {
                throw new PaymobApiException("API Request failed with status: " + statusCode, statusCode, responseBody);
            }

        } catch (JsonProcessingException e) {
            throw new PaymobException("Failed to serialize/deserialize JSON", e);
        } catch (IOException | InterruptedException e) {
            throw new PaymobException("Network error while communicating with Paymob API", e);
        }
    }
}
