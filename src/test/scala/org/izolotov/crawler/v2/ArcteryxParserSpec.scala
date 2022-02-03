package org.izolotov.crawler.v2

import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.scalatest.flatspec.AnyFlatSpec

class ArcteryxParserSpec extends AnyFlatSpec {

  implicit val httpClient: CloseableHttpClient = HttpClients.createDefault

  it should "..." in {
    val fetcher = new ChromeSeleniumFetcher("http://127.0.0.1:4444/wd/hub", "http://localhost:9222/json")
    val parser = new ArcteryxParser()
    val attempt = fetcher.fetch(SeleniumWebPage("https://arcteryx.com/ca/en/c/mens/shell-jackets"))
    val origin = parser.parse("https://arcteryx.com/ca/en/c/mens/shell-jackets", attempt)
    println(origin)
  }

}
