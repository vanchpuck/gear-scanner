package org.izolotov.crawler.timeoutscraper

trait Handler[Content, Document] {

  def handle(content: Content): Document
}
