//package org.izolotov.crawler.v2
//
//import java.time.Clock
//
//import org.apache.http.impl.client.HttpClients
//import org.scalatest.FlatSpec
//
//import scala.concurrent.ExecutionContext
//
//class FixedDelayDecoratorSpec extends FlatSpec{
//
//  it should ".." in {
//    val url = "https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/verevki-stropy-repshnury/-35009/?id=347256"
//    implicit val client = HttpClients.createDefault()
//    implicit val clock = Clock.systemUTC()
//    implicit val httpParser = new HttpParser()
//    implicit val fetcher = new HttpFetcher()
//    implicit val ec = ExecutionContext.global
//    val response = new FixedDelayDecorator[ScrapingAttempt[String]](new DefaultScraper(), 2000L)
////    new DefaultScraper()
//      .extract(new HttpWebPage(url))(fetcher, httpParser)
//      .response
//    println(response)
//
////      .map( f => println(f.response.get))
////    Thread.sleep(3000L)
//  }
//
//}
