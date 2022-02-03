//package org.izolotov.crawler.v3
//
//import java.time.Clock
//
//import org.apache.http.client.methods.CloseableHttpResponse
//import org.apache.http.impl.client.HttpClients
//import org.izolotov.crawler.v2.{ArcteryxParser, ChromeSeleniumFetcher, HttpResponse}
//
//class App {
//
//  implicit val client = HttpClients.createDefault()
//  implicit val clock = Clock.systemUTC()
////  implicit val fetcher: (String) => CloseableHttpResponse = new HttpFetcher().fetch
//
//  def f(str: String): (String) => ScrapingAttempt[_] = {
//    "sdf" match {
//      case "" => new DefaultScraper(new HttpFetcher().fetch, new HttpParser().parse).extract
//      case _ => new DefaultScraper(new ChromeSeleniumFetcher("", "").fetch, new ArcteryxParser().parse).extract
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//    val scraper: String = f("url").apply("df")
//    val d: (String => CloseableHttpResponse) = new HttpFetcher().fetch
////    new HostScraper[String]().extract("df")(f)
//    val x = 5
//
//  }
//
//}
