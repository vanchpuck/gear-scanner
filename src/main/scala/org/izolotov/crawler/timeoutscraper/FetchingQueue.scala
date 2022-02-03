package org.izolotov.crawler.timeoutscraper

import scala.concurrent.{ExecutionContext, Future}

trait FetchingQueue {

  def add[T](url: String, fetcher: (String) => T)(implicit executionContext: ExecutionContext): Future[T]

}
