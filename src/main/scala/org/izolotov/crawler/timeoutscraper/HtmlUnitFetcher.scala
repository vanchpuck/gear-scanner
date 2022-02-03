//package org.izolotov.crawler.timeoutscraper
//
//import java.net.URL
//import java.time.Clock
//
//import com.gargoylesoftware.htmlunit.WebClient
//import org.apache.http.impl.client.CloseableHttpClient
////import org.izolotov.crawler.timeoutscraper.SplashFetcher.Engine
//import org.openqa.selenium.WebDriver
//import com.gargoylesoftware.htmlunit.WebResponse
//import com.gargoylesoftware.htmlunit.html.HtmlPage
//
//import scala.tools.nsc.interpreter.InputStream
//
//object HtmlUnitFetcher {
//  case class Attempt(url: String, timestamp: Long, fetchTime: Long, httpCode: Int, response: InputStream)
//}
//
//class HtmlUnitFetcher(webClient: WebClient)(clock: Clock) {
//
//  def fetch(url: String): HtmlUnitFetcher.Attempt = {
//    val startTime = clock.instant.toEpochMilli
//    val page = webClient.getPage("http://www.google.com").asInstanceOf[HtmlPage]
//    val elapsedTime = clock.instant.toEpochMilli - startTime
//    val response = page.getWebResponse
//    HtmlUnitFetcher.Attempt(url, startTime, elapsedTime, response.getStatusCode, response.getContentAsStream)
//  }
//}
