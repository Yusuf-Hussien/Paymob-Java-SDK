package com.paymob.sdk.http;

import okhttp3.*;
import java.util.Map;

/**
 * Builder for creating HTTP requests with common patterns.
 * Provides fluent API for request construction.
 */
public class RequestBuilder {
    private final OkHttpClient client;
    private String baseUrl;
    private String method;
    private String endpoint;
    private Object requestBody;
    private Map<String, String> headers;

    public RequestBuilder(OkHttpClient client) {
        this.client = client;
    }

    public RequestBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RequestBuilder get(String endpoint) {
        this.method = "GET";
        this.endpoint = endpoint;
        return this;
    }

    public RequestBuilder post(String endpoint) {
        this.method = "POST";
        this.endpoint = endpoint;
        return this;
    }

    public RequestBuilder put(String endpoint) {
        this.method = "PUT";
        this.endpoint = endpoint;
        return this;
    }

    public RequestBuilder delete(String endpoint) {
        this.method = "DELETE";
        this.endpoint = endpoint;
        return this;
    }

    public RequestBuilder body(Object requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public RequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Request build() throws Exception {
        if (baseUrl == null || endpoint == null || method == null) {
            throw new IllegalArgumentException("Base URL, endpoint, and method are required");
        }

        String url = baseUrl + endpoint;
        Request.Builder requestBuilder = new Request.Builder().url(url);

        // Add headers
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                requestBuilder.header(header.getKey(), header.getValue());
            }
        }

        // Add request body
        if (requestBody != null && !method.equals("GET") && !method.equals("DELETE")) {
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(requestBody);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
            
            switch (method) {
                case "POST":
                    requestBuilder.post(body);
                    break;
                case "PUT":
                    requestBuilder.put(body);
                    break;
                case "DELETE":
                    requestBuilder.delete(body);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            }
        } else {
            switch (method) {
                case "GET":
                    requestBuilder.get();
                    break;
                case "DELETE":
                    requestBuilder.delete();
                    break;
            }
        }

        return requestBuilder.build();
    }
}
