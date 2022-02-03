//package org.izolotov.crawler.timeoutscraper
//
//import java.net.URL
//import java.util.concurrent.ConcurrentHashMap
//
//import scala.collection.JavaConverters._
//import scala.concurrent.{ExecutionContext, Future}
//
//class FixedDelayFetchingQueue[Fetched](delay: Long) /*extends FetchingQueue*/ {
//
//  private val queueMap = new ConcurrentHashMap[String, FixedDelayModerator]().asScala
//
////  val moderator = new FixedDelayModerator(delay)
//
//  def add(url: String, urlExtractor: (String) => Fetched)(implicit executionContext: ExecutionContext): Future[Fetched] = {
//    val host = new URL(url).getHost
//    val moderator = queueMap.getOrElseUpdate(
//      host,
//      new FixedDelayModerator(delay)
//    )
//    Future {
//      moderator.extract(url)
//    }
//  }
//
//  //  def extrac1t(url: String)(implicit fetcher: BaseFetcher[Response]): Future[Response] = {
//  //    val host = new URL(url).getHost
//  //    val queue = queueMap.getOrElseUpdate(
//  //      host,
//  //      queueFactory.apply()
//  //    )
//  //    queue.add(url, fetcher.fetch)
//  //  }
//}
