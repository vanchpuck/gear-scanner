//package org.izolotov.crawler.v2
//
//import org.izolotov.crawler.timeoutscraper.FixedDelayModerator
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class FixedDelayProxy[R](scraper: Scraper[R], delayMillis: Long)(implicit executionContext: ExecutionContext) extends Scraper[Future[R]] {
//
//  val moderator = new FixedDelayScraper(delayMillis)
//
//  override def extract[T <: Target, Raw](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, String]): Future[R] = {
//    moderator.extract(target, scraper..)
////    scraper.extract(target)
////  }
//}
