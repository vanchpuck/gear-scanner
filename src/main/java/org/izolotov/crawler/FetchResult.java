package org.izolotov.crawler;

import org.apache.http.HttpResponse;

/**
 * {@link HttpResponse} wrapper. In addition to response
 * contaions additional request meta info.
 * @param <T>
 */
public class FetchResult<T extends HttpResponse> {

    private final T response;

    private final long responseTime;

    public FetchResult(T response, long responseTime) {
        this.response = response;
        this.responseTime = responseTime;
    }

    public T getResponse() {
        return response;
    }

    public long getResponseTime() {
        return responseTime;
    }

}
