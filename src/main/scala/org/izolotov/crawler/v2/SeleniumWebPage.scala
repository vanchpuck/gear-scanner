package org.izolotov.crawler.v2

object SeleniumWebPage {
  def apply(url: String): SeleniumWebPage = new SeleniumWebPage(url)
}

class SeleniumWebPage(val url: String) extends Target
