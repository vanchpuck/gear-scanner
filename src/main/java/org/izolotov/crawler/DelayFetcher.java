package org.izolotov.crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DelayFetcher implements Fetcher<CloseableHttpResponse> {

    private static final Logger LOG = LogManager.getLogger(DelayFetcher.class);

    private final Lock delayLock = new ReentrantLock();
    private final Lock fetcherLock = new ReentrantLock();
    private final Condition lockedForTimeout = delayLock.newCondition();

    private volatile long prevFetchTime = 0;

    private class Delayer implements Runnable {

        private final long delay;

        public Delayer(long delay) {
            this.delay = delay;
        }

        @Override
        public void run() {
            delayLock.lock();
            try {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Delay thread interrupted", e);
                }
            } finally {
                lockedForTimeout.signal();
                delayLock.unlock();
            }
        }
    }

    private class Worker implements Callable<FetchAttempt<CloseableHttpResponse>> {

        private final String url;
        private final HttpGet httpGet;
        private final HttpContext httpContext;

        Worker(String url) {
            this(url, null);
        }

        Worker(String url, HttpContext httpContext) {
            this.url = url;
            this.httpGet = new HttpGet(url);
            this.httpContext = httpContext;
        }

        @Override
        public FetchAttempt<CloseableHttpResponse> call() throws Exception {
            long startTime = System.currentTimeMillis();
            try {
                LOG.info(String.format("GET %s %s", url, httpContext));
                CloseableHttpResponse response = httpClient.execute(httpGet, httpContext);
                long elapsedTime = System.currentTimeMillis() - startTime;
                LOG.info(String.format("Fetched %s %d %d", url, response.getStatusLine().getStatusCode(), elapsedTime));
                return new FetchAttempt<>(url, elapsedTime, response);
            } catch (IOException e) {
                LOG.warn(String.format("Failed %s %s", url, e.toString()));
                return new FetchAttempt(url, e);
            }
        }

        public void abort() {
            httpGet.abort();
        }
    }

    private final CloseableHttpClient httpClient;
    private final ExecutorService executor;

    public DelayFetcher(@Nonnull CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public FetchAttempt<CloseableHttpResponse> fetch(String url) {
        return fetch(url, 0L, Long.MAX_VALUE);
    }

    public FetchAttempt<CloseableHttpResponse> fetch(String url, long delay) {
        return fetch(url, delay, Long.MAX_VALUE);
    }

    public FetchAttempt<CloseableHttpResponse> fetch(String url, long delay, long timeout) {
        return fetch(url, delay, timeout, null);
    }

    public FetchAttempt<CloseableHttpResponse> fetch(String url, long delay, long timeout, HttpContext httpContext) {
        fetcherLock.lock();
        try {
            delayLock.lock();
            long nextFetchTime = prevFetchTime + delay;
            long remainingDelay = nextFetchTime - System.currentTimeMillis();
            if (System.currentTimeMillis() < nextFetchTime) {
                new Thread(new Delayer(remainingDelay)).start();
            }
            while (System.currentTimeMillis() < nextFetchTime) {
                try {
                    // Use the timed await just to be sure the deadlock will not arise
                    lockedForTimeout.await(remainingDelay, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Thread interrupted during delay pause", e);
                }
            }
            try {
                checkArgument(timeout >= 0, "Timeout can't be negative");
                checkNotNull(url, "Seed can't be null");
                Worker worker = null;
                try {
                    worker = new Worker(url, httpContext);
                    return executor.submit(worker).get(timeout, TimeUnit.MILLISECONDS);
                } catch (Exception exc) {
                    if (worker != null) {
                        worker.abort();
                    }
                    return new FetchAttempt(url, exc);
                }
            } finally {
                prevFetchTime = System.currentTimeMillis();
                delayLock.unlock();
            }
        } finally {
            fetcherLock.unlock();
        }
    }

}
