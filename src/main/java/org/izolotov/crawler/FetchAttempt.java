package org.izolotov.crawler;

import org.apache.http.HttpResponse;

import java.util.Optional;

/**
 * Created by izolotov on 17.11.18.
 */
public class FetchAttempt<T extends HttpResponse> {

    private final String url;
    private final long responseTime;
    private final T response;
    private final Exception exception;

    public FetchAttempt(String url, long responseTime, T response) {
        this.url = url;
        this.responseTime = responseTime;
        this.response = response;
        this.exception = null;
    }

    public FetchAttempt(String url, Exception exception) {
        this.url = url;
        this.responseTime = -1;
        this.response = null;
        this.exception = exception;
    }

    public String getUrl() {
        return url;
    }

    public Optional<T> getResponse() {
        return Optional.ofNullable(response);
    }

    public Optional<Long> getResponseTime() {
        return Optional.ofNullable(responseTime);
    }

    public Optional<Exception> getException() {
        return Optional.ofNullable(exception);
    }
}
