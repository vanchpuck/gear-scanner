//package org.izolotov.crawler.timeoutscraper
//
//import java.net.{URL, URLEncoder}
//import java.time.Clock
//
//import org.apache.http.impl.client.CloseableHttpClient
//import SplashFetcher._
//
//object SplashFetcher {
//  object Engine extends Enumeration {
//    type Engine = Value
//
//    val Chromium = Value("chromium")
//    val Webkit = Value("webkit")
//  }
//}
//
//class SplashFetcher(serverUrl: URL, engine: Engine.Engine = Engine.Chromium)(implicit httpClient: CloseableHttpClient, clock: Clock) extends BaseFetcher[HttpFetchAttempt] {
//
//  val httpFetcher = new HttpFetcher()
//
//  override def fetch(url: String): HttpFetchAttempt = {
//    // TODO check if we need url encoding
//    val apiCallUrl = new URL(serverUrl, s"render.html?engine=${engine}&url=${URLEncoder.encode(url, "UTF-8")}")
////    val apiCallUrl = new URL(serverUrl, s"render.html?engine=${engine}&url=${url}")
//    println(new URL(url).getPath)
//    val splashAttempt = httpFetcher.fetch(apiCallUrl.toString)
//    HttpFetchAttempt(url, splashAttempt.timestamp, splashAttempt.fetchTime, splashAttempt.response)
//  }
//}
