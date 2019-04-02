package org.izolotov.crawler;

import org.apache.http.HttpResponse;

public interface Fetcher<T extends HttpResponse> {

    FetchAttempt<T> fetch(String url) throws FetchException;

}
