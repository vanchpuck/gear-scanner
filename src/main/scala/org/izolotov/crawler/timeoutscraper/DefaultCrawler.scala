//package org.izolotov.crawler.timeoutscraper
//
//import java.time.Clock
//
//import scala.concurrent.{ExecutionContext, Future}
//
//object DefaultCrawler {
////  trait HttpResponse {
////    def content()
////
////    def
////  }
//}
//
//class DefaultCrawler(scraper: (String) => Future[HttpFetchAttempt], parser: (Future[HttpFetchAttempt]) => Int)(implicit clock: Clock) {
//
//  def crawl[Response, Document](url: String): String = {
//    val response = scraper.apply(url)
//    response.map(f => f.httpCode == 2)
////    val doc = parser.apply(response)
////    doc
//    "Ok"
////    val doc =
//  }
//
//
//}
