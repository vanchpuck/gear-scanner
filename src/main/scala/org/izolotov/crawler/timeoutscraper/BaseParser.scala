package org.izolotov.crawler.timeoutscraper

trait BaseParser[Response, Document] {

  def parse(content: Response): Document

}
