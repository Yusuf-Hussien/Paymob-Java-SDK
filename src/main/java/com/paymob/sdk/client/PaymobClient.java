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

    private <T> T sendRequest(String endpoint, String method, Object requestBody, Class<T> responseType) {
        try {
            String url = Paymob.getBaseUrl() + endpoint;
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(Paymob.getTimeoutSeconds()))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json");

            if ("POST".equalsIgnoreCase(method)) {
                String jsonBody = objectMapper.writeValueAsString(requestBody);
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
            } else if ("GET".equalsIgnoreCase(method)) {
                requestBuilder.GET();
            }

            // TODO: Add Authorization header logic here if needed globally,
            // but Paymob mostly uses body parameters or query params for auth tokens.

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode >= 200 && statusCode < 300) {
                return objectMapper.readValue(responseBody, responseType);
            } else {
                throw new PaymobApiException("Paymob API Error: " + statusCode, statusCode, responseBody);
            }

        } catch (JsonProcessingException e) {
            throw new PaymobException("Failed to serialize/deserialize JSON", e);
        } catch (IOException | InterruptedException e) {
            throw new PaymobException("Network error while communicating with Paymob API", e);
        }
    }
}
