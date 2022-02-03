package org.izolotov.crawler.v4

import java.net.URL

import org.openqa.selenium.WebDriver

class SeleniumFetcher extends Fetcher[WebDriver] {
  override def fetch(url: URL): WebDriver = {
    null
  }
}
