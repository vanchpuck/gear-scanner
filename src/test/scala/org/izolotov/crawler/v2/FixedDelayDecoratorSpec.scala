package org.izolotov.crawler.v2

import java.time.Clock

import org.apache.http.impl.client.HttpClients
import org.scalatest.FlatSpec

import scala.concurrent.ExecutionContext

class FixedDelayDecoratorSpec extends FlatSpec{

  it should ".." in {
    val url = "https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/verevki-stropy-repshnury/-35009/?id=347256"
    implicit val client = HttpClients.createDefault()
    implicit val clock = Clock.systemUTC()
    implicit val httpParser = new HttpParser()
    implicit val fetcher = new HttpFetcher()
    implicit val ec = ExecutionContext.global
    val underlyingScraper: Scraper[String, ScrapingAttempt[String]] = new DefaultScraper[String, ScrapingAttempt[String]]()
    val decorated = new FixedDelayDecorator[String, ScrapingAttempt[String]](underlyingScraper, 2000L)
//    val response = decorated.extract(new HttpWebPage(url))(fetcher, httpParser).response
    println(decorated.extract(new HttpWebPage(url))(fetcher, httpParser).response)
    println(decorated.extract(new HttpWebPage(url))(fetcher, httpParser).response)
    println(decorated.extract(new HttpWebPage(url))(fetcher, httpParser).response)

//      .map( f => println(f.response.get))
//    Thread.sleep(3000L)
  }

}
