//package org.izolotov.crawler.v4
//import java.net.URL
//import java.util.concurrent.ConcurrentHashMap
//
//import org.izolotov.crawler.v2.{FixedDelayDecorator, Scraper}
//
//import scala.collection.JavaConverters._
//import scala.concurrent.{ExecutionContext, Future}
//
//class HostCrawlingQueue[Content](defaultScraper: Scraper[Content], hostScrapers: Map[String, Scraper[Content]])
//                                (implicit executionContext: ExecutionContext) extends CrawlingQueue[ScrapingAttempt[Content]] {
//
//  private val hostMap = new ConcurrentHashMap[String, Scraper[Content]]().asScala
//
//  override def add(url: String): Future[ScrapingAttempt[Content]] = {
//    val host = new URL(url).getHost
//    val hostScraper = hostMap.getOrElseUpdate(
//      host, defaultScraper
//    )
//    Future {
//      hostScraper.extract(url)
//    }
//  }
//}
