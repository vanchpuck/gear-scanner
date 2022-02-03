package org.izolotov.crawler.timeoutscraper

import java.util.concurrent.{Executors, TimeUnit}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class TimeoutFetcher[T](scraper: BaseFetcher[T], timeout: Long) extends BaseFetcher[T] {

  override def fetch(url: String): T = {
    execute(scraper.fetch, url, timeout)
  }

  protected def execute(f: (String) => T, url: String, timeout: Long): T = {
    val executor = Executors.newSingleThreadExecutor()
    implicit val executionContext = ExecutionContext.fromExecutor(executor)
    try {
      val future = Future {
        f.apply(url)
      }
      Await.result(future, Duration.apply(timeout, TimeUnit.MILLISECONDS))
    } finally {
      executor.shutdownNow()
    }
  }
}
