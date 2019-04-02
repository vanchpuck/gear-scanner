package org.izolotov.crawler;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.Objects;

public class FetchStatus implements Serializable {

    private final Optional<Long> responseTime;
    private final Optional<Integer> httpStatusCode;
    private final Optional<String> reasonPhrase;
    private final Optional<Exception> exception;

    public FetchStatus(int httpStatusCode, String reasonPhrase, long responseTime) {
        this(httpStatusCode, reasonPhrase, responseTime, null);
    }

    public FetchStatus(Exception exception) {
        this(null, null, null, exception);
    }

    private FetchStatus(Integer httpStatusCode, String reasonPhrase, Long responseTime, Exception exc) {
        this.httpStatusCode = Optional.fromNullable(httpStatusCode);
        this.reasonPhrase = Optional.fromNullable(reasonPhrase);
        this.responseTime = Optional.fromNullable(responseTime);
        this.exception = Optional.fromNullable(exc);
    }

    public Optional<Integer> getHttpStatusCode() {
        return httpStatusCode;
    }

    public Optional<Long> getResponseTime() {
        return responseTime;
    }

    public Optional<String> getReasonPhrase() {
        return reasonPhrase;
    }

    public Optional<Exception> getException() {
        return exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FetchStatus that = (FetchStatus) o;
        return Objects.equals(responseTime, that.responseTime) &&
                Objects.equals(httpStatusCode, that.httpStatusCode) &&
                Objects.equals(reasonPhrase, that.reasonPhrase) &&
                Objects.equals(exception, that.exception);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseTime, httpStatusCode, reasonPhrase, exception);
    }
}
