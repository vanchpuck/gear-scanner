package org.izolotov.crawler.timeoutscraper

import java.io.{IOException, InputStream}
import java.net.URL
import java.time.Clock

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.cookie.ClientCookie
import org.apache.http.impl.client.{BasicCookieStore, CloseableHttpClient}
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.protocol.{BasicHttpContext, HttpContext}


object HttpFetcher {
  case class HttpFetchingAttempt(metadata: FetchingMetadata, content: InputStream) extends FetchingAttempt[FetchingMetadata, InputStream]
}

class HttpFetcher(cookies: Option[Map[String, _]] = None)(implicit httpClient: CloseableHttpClient, clock: Clock) extends BaseFetcher[CloseableHttpResponse] {

  override def fetch(url: String): CloseableHttpResponse = {
    val httpContext = newHttpContext(new URL(url).getHost, cookies.getOrElse(Map()));
    println(s"GET ${url} ${httpContext}")
    val httpGet = new HttpGet(url);
    val startTime = clock.instant.toEpochMilli
    val response: CloseableHttpResponse = httpClient.execute(httpGet, httpContext)
    val elapsedTime = clock.instant.toEpochMilli - startTime
//    println(s"Fetched ${url} ${response.getStatusLine.getStatusCode}")
//    HttpFetchAttempt(url, startTime, elapsedTime, response)
    response
  }

  def newHttpContext(host: String, cookies: Map[String, _]): HttpContext = {
    val cookieStore = new BasicCookieStore()
    cookies.foreach {
      cookieConf: (String, _) =>
        val cookie = new BasicClientCookie(cookieConf._1, cookieConf._2.toString);
        cookie.setDomain(host)
        cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true")
        cookie.setPath("/")
        cookieStore.addCookie(cookie)
    }
    val httpContext = new BasicHttpContext()
    httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore)
    httpContext
  }
}
