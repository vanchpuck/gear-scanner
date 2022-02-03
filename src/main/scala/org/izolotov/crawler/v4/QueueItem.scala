package org.izolotov.crawler.v4

trait QueueItem {

  def url(): String

  def onComplete(): Unit

  def queue(): CrawlingQueue

}
