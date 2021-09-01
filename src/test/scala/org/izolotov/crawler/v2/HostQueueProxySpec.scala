package org.izolotov.crawler.v2

import java.time.Clock
import java.util.concurrent.Executors

import org.apache.http.impl.client.HttpClients
import org.scalatest.FlatSpec

import scala.concurrent.ExecutionContext

class HostQueueProxySpec extends FlatSpec {

  it should "..." in {
    val url1 = "https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/verevki-stropy-repshnury/-35009/?id=347256"
    val url2 = "https://www.kant.ru/catalog/product/242758/?utm_source=google&utm_medium=cpc&utm_campaign=1738353287&utm_content=pla&utm_term=1055451&gclid=Cj0KCQjwpreJBhDvARIsAF1_BU2dNLlKoAE8A5hZagUjDHAJoNOrG45Efq7kt3XUhAvtC-FdSEb3sWoaAlBUEALw_wcB"
    implicit val client = HttpClients.createDefault()
    implicit val clock = Clock.systemUTC()
    implicit val httpParser = new HttpParser()
    implicit val fetcher = new HttpFetcher()
    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(12))
    def f(): Scraper[String, ScrapingAttempt[String]] = new DefaultScraper[String, ScrapingAttempt[String]]()
//    val factory: () => Scraper[String, ScrapingAttempt[String]] = new DefaultScraper[String, ScrapingAttempt[String]]()
    val scraper = new HostQueueScraper(2000L, f)
    println(scraper.extract(new HttpWebPage(url1))(fetcher, httpParser).onComplete(_ => println("Ok")))
    println(scraper.extract(new HttpWebPage(url1))(fetcher, httpParser).onComplete(_ => println("Ok")))
    println(scraper.extract(new HttpWebPage(url1))(fetcher, httpParser).onComplete(_ => println("Ok")))
    println(scraper.extract(new HttpWebPage(url2))(fetcher, httpParser).onComplete(_ => println("Ok!")))
    Thread.sleep(4000L)
  }

}
