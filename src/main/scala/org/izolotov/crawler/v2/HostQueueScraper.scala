package org.izolotov.crawler.v2

import java.net.URL
import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class HostQueueScraper[Doc, Attempt](delay: Long, scraperFactory: () => Scraper[Doc, Attempt])
                                  (implicit executionContext: ExecutionContext) extends Scraper[Doc, Future[Attempt]] {

  private val hostMap = new ConcurrentHashMap[String, Scraper[Doc, Attempt]]().asScala

  override def extract[T <: Target, Raw](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Future[Attempt] = {
    val host = new URL(target.url).getHost
    val hostScraper = hostMap.getOrElseUpdate(
      host, new FixedDelayDecorator[Doc, Attempt](scraperFactory.apply(), delay)
    )
    Future {
      hostScraper.extract(target)
    }
  }
}
