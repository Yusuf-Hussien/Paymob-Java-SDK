package com.paymob.sdk.exceptions;

public class PaymobApiException extends PaymobException {
    private final int statusCode;
    private final String responseBody;

    public PaymobApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
