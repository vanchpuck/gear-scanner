package org.izolotov.crawler.v4

import scala.concurrent.Future
//object CrawlingQueue {
//  def apply[A](scraper: A)(implicit processor: Processor[A]): CrawlingQueue[A] = {
//    new CrawlingQueue[A](scraper)
//  }
//}
//class CrawlingQueue(scraper: Scraper[_])(implicit processor: Processor[_]) {
trait DataSource {

  def read(): String

}
