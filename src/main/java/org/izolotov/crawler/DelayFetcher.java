package org.izolotov.crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

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
                    worker = new Worker(url);
                    System.out.println("Hit "+url);
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
