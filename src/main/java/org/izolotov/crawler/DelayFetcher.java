package org.izolotov.crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * This implementation allows to fetch URLs with specified delay between
 * the termination of one fetching and the commencement of the next.
 */
public class DelayFetcher implements Fetcher<CloseableHttpResponse>, Closeable {

    private static class DelayUrl implements Delayed {
        final String url;
        final long nextFetchTime;

        DelayUrl(String url, long nextFetchTime) {
            this.url = url;
            this.nextFetchTime = nextFetchTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long res = nextFetchTime - System.currentTimeMillis();
            return unit.convert(res, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }

    private class Worker implements Callable<FetchAttempt<CloseableHttpResponse>> {

        private final String url;
        private final HttpGet httpGet;

        Worker(String url) {
            this.url = url;
            this.httpGet = new HttpGet(url);
        }

        @Override
        public FetchAttempt<CloseableHttpResponse> call() throws Exception {
            long startTime = System.currentTimeMillis();
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                long elapsedTime = System.currentTimeMillis() - startTime;
                return new FetchAttempt<>(url, elapsedTime, response);
            } catch (IOException e) {
                return new FetchAttempt(url, e);
            }
        }

        public void abort() {
            httpGet.abort();
        }
    }

    private final CloseableHttpClient httpClient;
    private final DelayQueue<DelayUrl> queue;
    private final ExecutorService executor;
    private long nextFetchTime;
    private long delay;

    /**
     * Creates fetcher with no delay configured
     * @param httpClient {@link CloseableHttpClient} to send http requests
     */
    public DelayFetcher(@Nonnull CloseableHttpClient httpClient) {
        this(httpClient, 0L);
    }

    public DelayFetcher(@Nonnull CloseableHttpClient httpClient, long delay) {
        this.httpClient = httpClient;
        this.queue = new DelayQueue<>();
        this.nextFetchTime = 0;
        this.delay = delay;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Waits the delay time and starts the fetching. Fetching started
     * immediately on first method invocation.
     * @param url url to fetch
     * @return raw response
     */
    public FetchAttempt<CloseableHttpResponse> fetch(String url) {
        return fetch(url, Long.MAX_VALUE);
    }

    /**
     * Waits the delay time and starts the fetching.
     * Fetching started immediately on first method invocation.
     * @param url url to fetch
     * @param timeout fetching timeout
     * @return raw response
     */
    public FetchAttempt<CloseableHttpResponse> fetch(String url, long timeout) {
        checkArgument(timeout >= 0, "Timeout can't be negative");
        checkNotNull(url, "Seed can't be null");
        checkState(!executor.isShutdown(), "Fetcher has been closed");

        queue.add(new DelayUrl(url, nextFetchTime));
        Worker worker = null;
        try {
            worker = new Worker(queue.take().url);
            System.out.println("Hit "+url);
            return executor.submit(worker).get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception exc) {
            if (worker != null) {
                worker.abort();
            }
            return new FetchAttempt(url, exc);
        }
        finally {
            nextFetchTime = System.currentTimeMillis() + delay;
        }
    }

    /**
     * Sets the delay between the termination of one
     * execution and the commencement of the next
     * @param delay delay in millis
     */
    public void setDelay(@Nonnegative long delay) {
        checkArgument(delay >= 0, "Fetch delay can't be negative");
        this.nextFetchTime = nextFetchTime + (delay - this.delay);
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }

}
