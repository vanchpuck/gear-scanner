//package org.izolotov.crawler.v4
//
//abstract class WebScraper(implicit scrapingQueue: DataSource) {
//
//  scrapingQueue.addSubscriber(this)
//
//  def extractNext(): Unit = {
//    extract(scrapingQueue.extractNext())
//  }
//
//  abstract def extract(url: String): Unit
//
//}
