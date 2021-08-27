package org.izolotov.crawler.v2

import java.time.Clock

import org.apache.http.impl.client.HttpClients
import org.scalatest.FlatSpec

class ScraperSpec extends FlatSpec{

  it should ".." in {
    implicit val client = HttpClients.createDefault()
    implicit val clock = Clock.systemUTC()
    implicit val httpParser = new HttpParser()
    implicit val scraper = new HttpFetcher()
    val resp = new DefaultScraper[String]().extract(HttpWebPage("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/verevki-stropy-repshnury/-35009/?id=347256"))
    println(resp)
  }

}
