package org.izolotov.crawler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class DelayFetcherTest {

    public static final int PORT = 8091;

    private static final String REGULAR_URL = String.format("http://localhost:%d/regulat.html", PORT);
    private static final String DELAY_URL = String.format("http://localhost:%d/delay.html", PORT);
    private static final String CONNECTION_REFUSED_URL = String.format("http://localhost:%d/refused.html", PORT+1);
    private static final String UNKNOWN_HOST_URL = String.format("http://___unknown_host_test___/index.html", PORT+1);
    private static final String MALFORMED_URL = String.format("^^:*#!?,.localhost:%d/malformed.html", PORT+1);

    private static final long DELAY_IN_MILLIS = 2000L;
    private static final String DUMMY_CONTENT = "Dummy page";

    private static Server server;
    private static CloseableHttpClient httpClient;


    public static class RequestHandler extends AbstractHandler {

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            String url = baseRequest.getRequestURL().toString();
            if (DELAY_URL.equals(url)) {
                try {
                    Thread.sleep(DELAY_IN_MILLIS);
                } catch (InterruptedException exc) {
                    throw new RuntimeException(exc);
                }
            }
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().print(DUMMY_CONTENT);
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        httpClient = HttpClients.createDefault();
        server = new Server(PORT);
        server.setHandler(new RequestHandler());
        server.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void successFetchTest() throws Exception {
        String[] urls = {REGULAR_URL, REGULAR_URL, REGULAR_URL, REGULAR_URL, REGULAR_URL};
        DelayFetcher fetcher = new DelayFetcher(httpClient, 200L);
        for (String url : urls) {
            FetchAttempt<CloseableHttpResponse> actual = fetcher.fetch(url);
            assertThat(actual.getUrl(), is(url));
            assertThat(EntityUtils.toString(actual.getResponse().get().getEntity()), is(DUMMY_CONTENT));
            assertThat(actual.getException(), is(Optional.empty()));
            assertThat(actual.getResponseTime().get(), greaterThan(0L));
            actual.getResponse().get().close();
        }
    }

    @Test
    public void responseTimeTest() throws Exception {
        DelayFetcher fetcher = new DelayFetcher(httpClient);
        long startTime = System.currentTimeMillis();
        FetchAttempt<CloseableHttpResponse> response = fetcher.fetch(DELAY_URL);
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertThat("Response time should be greater than Jetty delay",
                response.getResponseTime().get(),  allOf(lessThanOrEqualTo(elapsedTime),
                        greaterThan(DELAY_IN_MILLIS)));
    }

    @Test
    public void firstFetchNoDelayTest() throws Exception {
        final long delay = 3000L;
        DelayFetcher fetcher = new DelayFetcher(httpClient, delay);
        long startTime = System.currentTimeMillis();
        fetcher.fetch(DELAY_URL);
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertThat("There must be no delay on first fetch", elapsedTime, lessThan(delay));
    }

    @Test
    public void delayTest() throws Exception {
        DelayFetcher fetcher = new DelayFetcher(httpClient);
        long prevFetchTime = System.currentTimeMillis();
        long currFetchTime;
        long sinceLastFetch;

        String[] urls = {REGULAR_URL, REGULAR_URL, REGULAR_URL};
        for (String url : urls) {
            FetchAttempt<CloseableHttpResponse> response = fetcher.fetch(url);
            currFetchTime = System.currentTimeMillis();
            sinceLastFetch = currFetchTime - prevFetchTime;
            prevFetchTime = currFetchTime;
            response.getResponse().get().close();

            assertThat("Time delay since last fetch should be greater or equal to fetch delay set",
                    sinceLastFetch, greaterThanOrEqualTo(fetcher.getDelay() + response.getResponseTime().get()));

            fetcher.setDelay(fetcher.getDelay()+200L);

        }
    }

    @Test
    public void timeoutTest() throws Exception {
        final long timeout = 500L;
        DelayFetcher fetcher = new DelayFetcher(httpClient, 100L);
        assertFalse("The non delayed page fetching must not be terminated by timeout",
                getFetchException(fetcher, REGULAR_URL, timeout).isPresent());
        assertThat("The delayed page fetching must be terminated by timeout",
                getFetchException(fetcher, DELAY_URL, timeout).get(), instanceOf(TimeoutException.class));
        assertFalse("The timeout occurred must not affect the following fetching",
                getFetchException(fetcher, REGULAR_URL, timeout).isPresent());
    }

    private static Optional<Exception> getFetchException(DelayFetcher fetcher , String url, long timeout) throws Exception {
        FetchAttempt<CloseableHttpResponse> attempt = fetcher.fetch(url, timeout);
        Optional<Exception> exc = attempt.getException();
        if (attempt.getResponse().isPresent()) {
            attempt.getResponse().get().close();
        }
        return exc;
    }

    @Test
    public void connectionRefusedTest() throws Exception {
        DelayFetcher fetcher = new DelayFetcher(httpClient);
        FetchAttempt<CloseableHttpResponse> response = fetcher.fetch(CONNECTION_REFUSED_URL);
        assertThat(response.getException().get(), instanceOf(HttpHostConnectException.class));
    }

    @Test
    public void unknownHostTest() throws Exception {
        DelayFetcher fetcher = new DelayFetcher(httpClient);
        FetchAttempt<CloseableHttpResponse> response = fetcher.fetch(UNKNOWN_HOST_URL);
        assertThat(response.getException().get(), instanceOf(UnknownHostException.class));
    }

    @Test
    public void malformedUrlTest() throws Exception {
        DelayFetcher fetcher = new DelayFetcher(httpClient);
        FetchAttempt<CloseableHttpResponse> response = fetcher.fetch(MALFORMED_URL);
        assertThat(response.getException().get(), instanceOf(IllegalArgumentException.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDelayTest() throws Exception {
        DelayFetcher fetcher = new DelayFetcher(httpClient);
        fetcher.setDelay(-1L);
    }

}

