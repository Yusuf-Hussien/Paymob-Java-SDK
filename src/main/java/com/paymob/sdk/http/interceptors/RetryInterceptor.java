package com.paymob.sdk.http.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Interceptor for implementing exponential backoff retry logic.
 * Retries failed requests with increasing delays.
 */
public class RetryInterceptor implements Interceptor {
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 1000; // 1 second
    private static final long MAX_BACKOFF_MS = 10000; // 10 seconds

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;

        for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
            try {
                if (attempt > 0) {
                    long backoffMs = Math.min(INITIAL_BACKOFF_MS * (1L << (attempt - 1)), MAX_BACKOFF_MS);
                    Thread.sleep(backoffMs);
                }

                response = chain.proceed(request);

                // If successful or client error (4xx), don't retry
                if (response.isSuccessful() || (response.code() >= 400 && response.code() < 500)) {
                    return response;
                }

                // Close response body for retry
                if (response.body() != null) {
                    response.body().close();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Request interrupted", e);
            } catch (IOException e) {
                lastException = e;

                // Don't retry on certain exceptions
                if (!shouldRetry(e)) {
                    throw e;
                }
            }
        }

        // All retries exhausted
        if (lastException != null) {
            throw lastException;
        }

        return response;
    }

    private boolean shouldRetry(IOException exception) {
        // Don't retry on authentication errors, SSL errors, etc.
        String message = exception.getMessage();
        if (message == null) {
            return true;
        }

        return !message.contains("401") &&
                !message.contains("403") &&
                !message.contains("SSL") &&
                !message.contains("certificate");
    }
}
