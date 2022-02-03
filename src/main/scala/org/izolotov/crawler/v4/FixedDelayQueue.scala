package org.izolotov.crawler.v4
import scala.concurrent.{ExecutionContext, Future}

class FixedDelayQueue(delay: Long)(implicit ec: ExecutionContext) extends ScrapingQueue {

  val moderator = new FixedDelayModerator(delay)

  override def add[A](url: String, scraper: String => A): Future[A] = {
    Future {
      moderator.extract(url, scraper)
    }
  }
}
