package org.izolotov.crawler.v4

import java.net.URL
import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class PerHostQueue[A](hostQueueFactory: (String) => ScrapingQueue) extends ScrapingQueue {

  private val queueMap = new ConcurrentHashMap[String, ScrapingQueue]().asScala

  override def add[A](url: String, scraper: String => A): Future[A] = {
    queueMap.getOrElseUpdate(new URL(url).getHost, hostQueueFactory.apply(url))
      .add(url, scraper)
  }
}
