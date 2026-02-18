package com.paymob.sdk.http.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import java.io.IOException;

/**
 * Interceptor for logging HTTP requests and responses.
 * Uses OkHttp's built-in logging interceptor with configurable levels.
 */
public class LoggingInterceptor implements Interceptor {
    private final HttpLoggingInterceptor.Level logLevel;
    private final HttpLoggingInterceptor logger;

    public LoggingInterceptor() {
        this(HttpLoggingInterceptor.Level.NONE);
    }

    public LoggingInterceptor(HttpLoggingInterceptor.Level logLevel) {
        this.logLevel = logLevel;
        this.logger = new HttpLoggingInterceptor()
                .setLevel(logLevel);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return logger.intercept(chain);
    }

    public void setLogLevel(HttpLoggingInterceptor.Level logLevel) {
        this.logger.setLevel(logLevel);
    }
}
