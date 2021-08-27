//package org.izolotov.crawler.v2
//
//import java.time.Clock
//
//import org.apache.http.impl.client.HttpClients
//
//object Main {
//
//  def execute(): Unit = {
//    implicit val client = HttpClients.createDefault()
//    implicit val clock = Clock.systemUTC()
//    implicit val httpParser = new HttpParser()
//    implicit val scraper = new HttpFetcher()
//
//    new DefaultScraper().extract(HttpWebPage("url"))
//  }
//
//}
